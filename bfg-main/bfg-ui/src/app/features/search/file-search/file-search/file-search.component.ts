import { Component, OnInit } from '@angular/core';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { FormGroup, FormBuilder } from '@angular/forms';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FileCriteriaData } from 'src/app/shared/models/file/file-criteria.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { File } from 'src/app/shared/models/file/file.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { FILE_DIRECTIONS, getDirectionBooleanValue, getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { getFileStatusIcon, FILE_STATUS_ICON } from 'src/app/shared/models/file/file-status-icon';
import { get } from 'lodash';
import * as moment from 'moment';

@Component({
  selector: 'app-file-search',
  templateUrl: './file-search.component.html',
  styleUrls: ['./file-search.component.scss']
})
export class FileSearchComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;
  getFileStatusIcon = getFileStatusIcon;
  FILE_STATUS_ICON = FILE_STATUS_ICON;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  searchingParametersFormGroup: FormGroup;
  fileCriteriaData: FileCriteriaData;

  defaultSelectedData: string[] = [
    moment().subtract(1, 'months').hours(0).minutes(0).seconds(0).format('YYYY-MM-DDTHH:mm:ss'),
    moment().add(1, 'days').hours(23).minutes(59).seconds(0).format('YYYY-MM-DDTHH:mm:ss')
  ];

  selectedData: string[];

  criteriaFilterObject = { direction: '', service: '' };

  files: FilesWithPagination;
  displayedColumns: string[] = [
    'status',
    'id',
    'fileName',
    'reference',
    'type',
    'service',
    'timestamp',
    'WFID',
    'error',
    'transactions'
  ];
  dataSource: MatTableDataSource<File>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private formBuilder: FormBuilder,
    private fileService: FileService
  ) {
    this.selectedData = this.defaultSelectedData;
  }

  ngOnInit(): void {
    this.initializeSearchingParametersFormGroup();
    this.getFileCriteriaData();
  }

  initializeSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      entity: [''],
      service: [''],
      direction: [''],
      fileStatus: [''],
      bpState: [''],
      fileName: [''],
      reference: [''],
      type: [''],
      from: [this.defaultSelectedData],
      to: [this.defaultSelectedData]
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
    } else{
      contol.setValue('');
    }
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }


  getFileList(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.errorMessage = null;

    const formData = {
      ...this.searchingParametersFormGroup.value,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('from'), 'value[0]')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('to'), 'value[1]')),
      outbound: getDirectionBooleanValue(get(this.searchingParametersFormGroup.get('direction'), 'value')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };
    formData.status = formData.fileStatus.status;
    formData.fileStatus = null;
    formData.direction = null;

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

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  setServiceAndDirectionFromStatus(fromStatus){
    if (fromStatus !== ''){
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

      if ( newDirection && (initialDirection !== newDirection) ){
        directionControl.setValue(newDirection);
        refreshRequired = true;
        this.criteriaFilterObject.direction = newDirection.toUpperCase();
      }
      if ( newService && (initialService !== newService) ){
        serviceControl.setValue(newService);
        refreshRequired = true;
        this.criteriaFilterObject.service = newService;
      }
      if (refreshRequired){
        this.getFileCriteriaData();
      }
    }
  }

}
