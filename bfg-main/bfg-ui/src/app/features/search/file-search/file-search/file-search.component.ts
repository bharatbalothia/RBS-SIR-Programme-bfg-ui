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

@Component({
  selector: 'app-file-search',
  templateUrl: './file-search.component.html',
  styleUrls: ['./file-search.component.scss']
})
export class FileSearchComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  searchingParametersFormGroup: FormGroup;
  fileCriteriaData: FileCriteriaData;

  files: FilesWithPagination;
  displayedColumns: string[] = [
    'select',
    'status',
    'id',
    'fileName',
    'reference',
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
  ) { }

  ngOnInit(): void {
    this.initializeSearchingParametersFormGroup();
    this.getFileCriteriaData();
  }

  initializeSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      fileStatus: [''],
      type: [''],
    });
  }

  getFileCriteriaData = () => this.fileService.getFileCriteriaData()
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
}
