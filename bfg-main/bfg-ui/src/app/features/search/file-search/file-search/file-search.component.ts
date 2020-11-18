import { Component, OnInit, EventEmitter, ViewChild } from '@angular/core';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FileCriteriaData } from 'src/app/shared/models/file/file-criteria.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { File } from 'src/app/shared/models/file/file.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { FILE_DIRECTIONS, getDirectionBooleanValue, getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { STATUS_ICON } from 'src/app/core/constants/status-icon';
import { get } from 'lodash';
import * as moment from 'moment';
import { FileTableComponent } from 'src/app/shared/components/file-table/file-table.component';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';

@Component({
  selector: 'app-file-search',
  templateUrl: './file-search.component.html',
  styleUrls: ['./file-search.component.scss']
})
export class FileSearchComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;
  FILE_STATUS_ICON = STATUS_ICON;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  @ViewChild(FileTableComponent)
  fileTableComponent: FileTableComponent;

  searchingParametersFormGroup: FormGroup;
  fileCriteriaData: FileCriteriaData;

  defaultSelectedData: { from: { startDate, endDate }, to: { startDate, endDate } } = {
    from: {
      startDate: moment().subtract(1, 'days').hours(0).minutes(0).seconds(0),
      endDate: moment().subtract(1, 'days').hours(0).minutes(0).seconds(0)
    },
    to: {
      startDate: moment().add(1, 'days').hours(23).minutes(59).seconds(0),
      endDate: moment().add(1, 'days').hours(23).minutes(59).seconds(0)
    }

  };

  criteriaFilterObject = { direction: '', service: '' };

  files: FilesWithPagination;
  dataSource: MatTableDataSource<File>;
  pageIndex = 0;
  pageSize = 100;

  constructor(
    private formBuilder: FormBuilder,
    private fileService: FileService,
    private toolTip: TooltipService,
  ) { }

  ngOnInit(): void {
    this.initializeSearchingParametersFormGroup();
    this.getFileCriteriaData();
  }

  initializeSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      entityId: [''],
      service: [''],
      direction: [''],
      fileStatus: [''],
      bpstate: [''],
      filename: [''],
      reference: [''],
      type: [''],
      from: [this.defaultSelectedData.from],
      to: [this.defaultSelectedData.to]
    });
    this.criteriaFilterObject = { direction: '', service: '' };
  }

  getFileCriteriaData = () => {
    const filterObject = {
      ...this.criteriaFilterObject,
      outbound: this.criteriaFilterObject.direction === '' ? '' : this.getDirectionValue(this.criteriaFilterObject.direction.toUpperCase())
    };
    filterObject.direction = null;

    this.fileService.getFileCriteriaData(removeEmpties(filterObject))
      .pipe(data => this.setLoading(data))
      .subscribe((data: FileCriteriaData) => {
        this.isLoading = false;
        this.fileCriteriaData = data;
        this.persistSelectedFileStatus();
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }
  persistSelectedFileStatus() {
    const contol = this.searchingParametersFormGroup.controls.fileStatus;
    const initialStatus = contol.value;
    const valueInData = this.fileCriteriaData.fileStatus.find((val) => JSON.stringify(val) === JSON.stringify(initialStatus));
    if (valueInData) {
      contol.setValue(valueInData);
    } else {
      contol.setValue('');
    }
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }


  getFileList(pageIndex: number, pageSize: number) {
    this.errorMessage = null;

    const formData = {
      ...this.searchingParametersFormGroup.value,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('from'), 'value.startDate', null)),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('to'), 'value.endDate', null)),
      outbound: getDirectionBooleanValue(get(this.searchingParametersFormGroup.get('direction'), 'value')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };
    formData.status = formData.fileStatus.status;
    formData.fileStatus = null;
    formData.direction = null;

    this.isLoading = true;
    this.fileService.getFileList(removeEmpties(formData)).pipe(take(1)).subscribe((data: FilesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.files = data;
      this.updateTable();
    },
      (error) => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
  }

  convertDateToFormat = (date: string) => moment(date).isValid() ? moment(date).format('YYYY-MM-DDTHH:mm:ss') : null;

  updateTable() {
    this.dataSource = new MatTableDataSource(this.files.content);
  }

  onStepChange(event) {
    if (event.selectedIndex === 1) {
      this.getFileList(this.pageIndex, this.pageSize);
      this.fileTableComponent.autoRefreshChange(true);
    }
    else {
      this.fileTableComponent.autoRefreshChange(false);
    }

  }

  resetSearchParameters = () => {
    this.initializeSearchingParametersFormGroup();
    this.getFileCriteriaData();
  }

  onServiceSelect = (event) => {
    this.criteriaFilterObject.service = event.value;
    this.getFileCriteriaData();
  }

  onDirectionSelect = (event) => {
    this.criteriaFilterObject.direction = event.value;
    this.getFileCriteriaData();
  }

  onStatusSelect = (event) => {
    this.setServiceAndDirectionFromStatus(event.value);
  }

  getDirectionValue = (direction) => direction === FILE_DIRECTIONS.OUTBOUND;

  setServiceAndDirectionFromStatus(fromStatus) {
    if (fromStatus !== '') {
      let refreshRequired = false;
      const serviceControl = this.searchingParametersFormGroup.controls.service;
      const initialService = serviceControl.value;
      const directionControl = this.searchingParametersFormGroup.controls.direction;
      const initialDirection = directionControl.value;

      const newService = this.fileCriteriaData.service.find((element) =>
        element.toUpperCase() === fromStatus.service.toUpperCase()
      );

      const newDirection = this.fileCriteriaData.direction.find((element) =>
        element.toUpperCase() === getDirectionStringValue(fromStatus.outbound)
      );

      if (newDirection && (initialDirection !== newDirection)) {
        directionControl.setValue(newDirection);
        refreshRequired = true;
        this.criteriaFilterObject.direction = newDirection.toUpperCase();
      }
      if (newService && (initialService !== newService)) {
        serviceControl.setValue(newService);
        refreshRequired = true;
        this.criteriaFilterObject.service = newService;
      }
      if (refreshRequired) {
        this.getFileCriteriaData();
      }
    }
  }

  isInvalidFromDate = (date) => {
    const endDate = get(this.searchingParametersFormGroup.get('to'), 'value.endDate', null);
    return endDate && moment(endDate).isBefore(date);
  }

  isInvalidToDate = (date) => {
    const startDate = get(this.searchingParametersFormGroup.get('from'), 'value.startDate', null);
    return startDate && moment(date).isBefore(startDate);
  }

  onDateChange = (event, control: AbstractControl) => {
    if (get(event, 'target.value', null) === '') {
      control.setValue(null);
    }
  }

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'file-search',
      qualifier: step,
      mode: 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getFileSearchDisplayName(field);
  }
}
