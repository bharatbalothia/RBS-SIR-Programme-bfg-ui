import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FileCriteriaData } from 'src/app/shared/models/file/file-criteria.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { File } from 'src/app/shared/models/file/file.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { map, startWith, take } from 'rxjs/operators';
import { FILE_DIRECTIONS, getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { STATUS_ICON } from 'src/app/core/constants/status-icon';
import { get, isEmpty, isFunction } from 'lodash';
import * as moment from 'moment';
import { FileTableComponent } from 'src/app/shared/components/file-table/file-table.component';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { ActivatedRoute } from '@angular/router';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-file-search',
  templateUrl: './file-search.component.html',
  styleUrls: ['./file-search.component.scss']
})
export class FileSearchComponent implements OnInit, AfterViewInit {

  @ViewChild(FileTableComponent)
  fileTableComponent: FileTableComponent;

  @ViewChild('stepper') stepper: MatHorizontalStepper;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;

  searchingParametersFormGroup: FormGroup;
  fileCriteriaData: FileCriteriaData;
  filteredEntityList: Observable<{ entityName: string, entityId: number }[]>;

  getSearchValidationMessage = getSearchValidationMessage;
  getFileSearchDisplayName = getFileSearchDisplayName;
  FILE_STATUS_ICON = STATUS_ICON;

  isLinear = true;
  isLoading = false;

  defaultSelectedData: { from: moment.Moment, to: moment.Moment } = {
    from: moment().subtract(1, 'days').startOf('day'),
    to: moment().add(1, 'days').endOf('day')
  };

  criteriaFilterObject = { direction: '', service: '' };

  files: FilesWithPagination;
  dataSource: MatTableDataSource<File>;
  pageIndex = 0;
  pageSize = 100;

  URLParams;

  constructor(
    private formBuilder: FormBuilder,
    private fileService: FileService,
    private toolTip: TooltipService,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.URLParams = { ...params };
      if (params.startDate) {
        this.defaultSelectedData = {
          from: params.startDate === 'none' ? null : moment(params.startDate),
          to: null
        };
        delete this.URLParams.startDate;
      }
      this.initializeSearchingParametersFormGroup();
      this.getFileCriteriaData();
    });
  }

  ngAfterViewInit() {
    if (!this.isURLParamsEmpty()) {
      this.onNext();
      this.cdr.detectChanges();
    }
    setTimeout(() => {
      this.stepper.steps.forEach((step, idx) => {
        step.select = () => {
          if (idx === 1 && this.searchingParametersFormGroup.valid) {
            this.getFileList(0, this.pageSize, () => this.stepper.selectedIndex = idx);
          }
          else {
            this.stepper.selectedIndex = idx;
          }
        };
      });
    });
  }

  isURLParamsEmpty = () => isEmpty(this.URLParams);

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
    this.initMinMaxDate();
  }

  initMinMaxDate() {
    this.minDate = this.defaultSelectedData.from;
    this.maxDate = this.defaultSelectedData.to;
    this.searchingParametersFormGroup.controls.from.markAsTouched();
    this.searchingParametersFormGroup.controls.to.markAsTouched();
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
        this.fileCriteriaData = {
          ...data,
          entity: [
            { entityName: 'ALL', entityId: 0 },
            ...data.entity
          ]
        };
        this.filteredEntityList = this.searchingParametersFormGroup.controls.entityId.valueChanges
          .pipe(
            startWith(''),
            map(value => this._filterEntityList(value))
          );
        this.persistSelectedFileStatus();
      },
        error => this.isLoading = false
      );
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
    this.isLoading = true;
    return data;
  }

  getFileList(pageIndex: number, pageSize: number, callback?) {
    const formData = {
      ...this.searchingParametersFormGroup.value,
      ...!isEmpty(this.URLParams) && this.URLParams,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };

    // Don't send 'All' to backend. Check if entityId is 0 ('ALL')
    if (formData.entityId === 0) {
      delete formData.entityId;
    }

    formData.direction = formData.direction && !Array.isArray(formData.direction) ? [formData.direction.toLowerCase()] : formData.direction;
    formData.status = get(formData, 'fileStatus.status');
    formData.fileStatus = null;

    this.isLoading = true;
    this.fileService.getFileList(removeEmpties(formData)).pipe(take(1)).subscribe((data: FilesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.files = data;
      this.updateTable();
      if (isFunction(callback)) {
        callback();
      }
    },
      error => this.isLoading = false
    );
  }

  convertDateToFormat = (date: moment.Moment | null) => date && date.format('YYYY-MM-DDTHH:mm:ss');

  updateTable() {
    this.dataSource = new MatTableDataSource(this.files.content);
  }

  onStepChange(event) {
    if (event.selectedIndex === 1) {
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

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'file-search',
      qualifier: step,
      mode: 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getFileSearchDisplayName(field);
  }

  handleDate(event, field: string) {
    const date: moment.Moment = event.value;
    if (date) {
      this[field] = date;
    }
  }

  onNext = () => this.getFileList(0, this.pageSize, () => this.stepper.next());

  displayEntity(value?: number) {
    return value ? this.fileCriteriaData.entity.find(entity => entity.entityId === value).entityName : 'ALL';
  }

  private _filterEntityList(value: string | number) {
    if (typeof value === 'string') {
      return value ?
        this.fileCriteriaData.entity.filter(option => option.entityName.toLowerCase().includes(value.toLowerCase())) :
        this.fileCriteriaData.entity;
    } else {
      return value ?
        this.fileCriteriaData.entity.filter(option => option.entityId === value) : this.fileCriteriaData.entity;
    }
  }
}
