import { Component, OnInit, Inject, EventEmitter } from '@angular/core';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { FileService } from 'src/app/shared/models/file/file.service';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { get } from 'lodash';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { getTransactionDetailsTabs, getTransactionDocumentInfoTabs } from '../../transaction-search/transaction-search-display-names';
import { BusinessProcessDialogComponent } from 'src/app/shared/components/business-process-dialog/business-process-dialog.component';
import { getBusinessProcessDisplayName } from 'src/app/shared/models/business-process/business-process-display-names';
import { BusinessProcessDialogConfig } from 'src/app/shared/components/business-process-dialog/business-process-dialog-config.model';

@Component({
  selector: 'app-transactions-dialog',
  templateUrl: './transactions-dialog.component.html',
  styleUrls: ['./transactions-dialog.component.scss']
})
export class TransactionsDialogComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;

  displayName: (fieldName: string) => string;

  errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};

  isLoading = true;
  errorMessage: ErrorMessage;

  transactions: TransactionsWithPagination;
  displayedColumns: string[] = ['id', 'transactionID', 'settleDate', 'settleAmount', 'type', 'status', 'workflowID'];
  dataSource: MatTableDataSource<Transaction>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  fileId: number;
  actions;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private fileService: FileService,
    private dialog: MatDialog
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.fileId = get(this.data, 'actionData.fileId');
    this.actions = this.actions = get(this.data, 'actionData.actions');
  }

  ngOnInit() {
    this.getTransactions(this.pageIndex, this.pageSize);
  }

  getTransactions(pageIndex: number, pageSize: number) {
    this.fileService.getTransactionListByFileId(this.fileId, { page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1))
      .pipe(data => this.setLoading(data))
      .subscribe((data: TransactionsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.transactions = data;
        this.updateTable();
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

  openTransactionDetailsDialog = (fileId: number, id: number) => {
    this.createErrorMessageEmitter(id);
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction -  ${data.id}`,
          tabs: getTransactionDetailsTabs(data, ...this.actions),
          displayName: getFileSearchDisplayName,
          isDragable: true,
          actionData: {
            actions: {
              ...this.actions
            }
          },
          parentError: this.errorMessageEmitters[id]
        })).afterClosed().subscribe(() => this.deleteErrorMessageEmitter(fileId));
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  openTransactionDocumentInfo = (transaction: Transaction) => this.fileService.getDocumentContent(transaction.docID)
    .pipe(data => this.setLoading(data))
    .subscribe((data: DocumentContent) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Transaction Document Information`,
        tabs: getTransactionDocumentInfoTabs({ ...data, processID: transaction.workflowID }),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
        this.emitErrorMessageEvent(transaction.id);
      })

  openBusinessProcessDialog = (transaction: Transaction) =>
    this.dialog.open(BusinessProcessDialogComponent, new BusinessProcessDialogConfig({
      title: `Business Process Detail`,
      tabs: [],
      displayName: getBusinessProcessDisplayName,
      isDragable: true,
      actionData: {
        id: transaction.workflowID,
        actions: {
        }
      },
    }))

  createErrorMessageEmitter(id: number) {
    this.errorMessageEmitters[id] = new EventEmitter<ErrorMessage>();
  }

  deleteErrorMessageEmitter(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id] = null;
    }
  }

  emitErrorMessageEvent(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id].emit(this.errorMessage);
    }
  }
}
