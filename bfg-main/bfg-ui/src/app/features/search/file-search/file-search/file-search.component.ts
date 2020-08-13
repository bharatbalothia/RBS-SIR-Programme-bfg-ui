import { Component, OnInit } from '@angular/core';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { FormGroup, FormBuilder } from '@angular/forms';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FileCriteriaData } from 'src/app/shared/models/file/file-criteria.model';
import { getFileSearchDisplayName, getFileDetailsTabs, getFileTransactionsTabs, getTransactionDetailsTabs, getErrorDetailsTabs } from '../file-search-display-names';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { File } from 'src/app/shared/models/file/file.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { getDirectionBooleanValue } from 'src/app/shared/models/file/file-directions';
import { getFileStatusIcon, FILE_STATUS_ICON } from 'src/app/shared/models/file/file-status-icon';
import { get } from 'lodash';
import * as moment from 'moment';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { TransactionsWithPagination } from 'src/app/shared/models/file/transactions-with-pagination.model';
import { Transaction } from 'src/app/shared/models/file/transaction.model';
import { FileError } from 'src/app/shared/models/file/file-error.model';
import { TransactionsDialogComponent } from '../transactions-dialog/transactions-dialog.component';

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
    moment().add(1, 'days').hours(11).minutes(59).seconds(0).format('YYYY-MM-DDTHH:mm:ss')
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
    private fileService: FileService,
    private dialog: MatDialog
  ) {
    this.selectedData = this.defaultSelectedData;
  }

  ngOnInit(): void {
    this.initializeSearchingParametersFormGroup();
    this.getFileCriteriaData();
  }

  initializeSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      entityID: [''],
      service: [''],
      direction: [''],
      status: [''],
      bpState: [''],
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
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }


  getFileList(pageIndex: number, pageSize: number) {
    this.errorMessage = null;
    this.fileService.getFileList(removeEmpties({
      ...this.searchingParametersFormGroup.value,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('from'), 'value[0]')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('to'), 'value[1]')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(data => this.setLoading(data)).pipe(take(1)).subscribe((data: FilesWithPagination) => {
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

  onServiceSelect = (event) => this.getFileCriteriaData({ service: event.value });

  onDirectionSelect = (event) => this.getFileCriteriaData({ outbound: getDirectionBooleanValue(event.value) });

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  openFileDetailsDialog = (file: File) =>
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `File - ${file.id}`,
      tabs: getFileDetailsTabs(file),
      displayName: getFileSearchDisplayName,
      isDragable: true,
      actionData: {
        actions: {
          errorCode: () => this.openErrorDetailsDialog(file),
          transactionTotal: () => this.openTransactionsDialog(file)
        }
      }
    }))

  openTransactionsDialog = (file: File) =>
    this.dialog.open(TransactionsDialogComponent, new DetailsDialogConfig({
      title: `Transactions for ${file.filename} [${file.id}]`,
      tabs: [],
      displayName: getFileSearchDisplayName,
      isDragable: true,
      actionData: {
        fileId: file.id
      }
    }))

  openErrorDetailsDialog = (file: File) => this.fileService.getErrorDetailsByCode(file.errorCode)
    .pipe(data => this.setLoading(data))
    .subscribe((data: FileError) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${data.code}`,
        tabs: getErrorDetailsTabs(data),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      })
}
