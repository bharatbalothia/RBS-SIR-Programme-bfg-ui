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
import { FILE_DIRECTIONS } from 'src/app/shared/models/file/file-directions';
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
    moment().subtract(1, 'months').hours(0).minutes(0).seconds(0).toISOString(),
    moment().add(1, 'days').hours(11).minutes(59).seconds(0).toISOString()
  ];

  selectedData: string[];

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
      bpstate: [''],
      fileName: [''],
      reference: [''],
      type: [''],
      from: [this.defaultSelectedData],
      to: [this.defaultSelectedData]
    });
  }

  getFileCriteriaData = (params?: { outbound?, service?: string }) =>
    this.fileService.getFileCriteriaData(removeEmpties(params))
      .pipe(data => this.setLoading(data))
      .subscribe((data: FileCriteriaData) => {
        this.isLoading = false;
        this.fileCriteriaData = data;
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        })

  setLoading(data) {
    this.isLoading = true;
    return data;
  }


  getFileList(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.errorMessage = null;
    this.fileService.getFileList(removeEmpties({
      ...this.searchingParametersFormGroup.value,
      from: get(this.searchingParametersFormGroup.get('from'), 'value[0]'),
      to: get(this.searchingParametersFormGroup.get('to'), 'value[1]'),
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).subscribe((data: FilesWithPagination) => {
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

  onServiceSelect = (event) => this.getFileCriteriaData({ service: event.value });

  onDirectionSelect = (event) => this.getFileCriteriaData({ outbound: this.getDirectionValue(event.value.toUpperCase()) });

  getDirectionValue = (direction) => direction === FILE_DIRECTIONS.OUTBOUND;

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

}
