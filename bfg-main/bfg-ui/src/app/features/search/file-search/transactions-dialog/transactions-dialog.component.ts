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
import { FileDialogService } from 'src/app/shared/models/file/file-dialog.service';

@Component({
  selector: 'app-transactions-dialog',
  templateUrl: './transactions-dialog.component.html',
  styleUrls: ['./transactions-dialog.component.scss']
})
export class TransactionsDialogComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;

  displayName: (fieldName: string) => string;

  errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};
  isLoadingEmitters: { [id: number]: EventEmitter<boolean> } = {};

  isLoading = true;
  errorMessage: ErrorMessage;

  transactions: TransactionsWithPagination;
  displayedColumns: string[] = ['id', 'transactionID', 'settleDate', 'settleAmount', 'type', 'status', 'workflowID'];
  dataSource: MatTableDataSource<Transaction>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  fileId: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private fileService: FileService,
    private fileDialogService: FileDialogService,
    private dialog: MatDialog
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.fileId = get(this.data, 'actionData.fileId');
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
    this.createEmitters(id);
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        const actions: any = {
          file: () => this.openFileDetailsDialog({ id: fileId }, id),
          workflowID: () => this.openBusinessProcessDialog(data)
        };
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction -  ${data.id}`,
          tabs: getTransactionDetailsTabs(data, ...actions),
          displayName: getFileSearchDisplayName,
          isDragable: true,
          actionData: {
            actions
          },
          parentError: this.errorMessageEmitters[id],
          parentLoading: this.isLoadingEmitters[id],
        })).afterClosed().subscribe(() => this.deleteEmitters(id));
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  openFileDetailsDialog = (file, transactionId) => {
    this.fileDialogService.errorMessageChange.subscribe(data => {
      this.errorMessage = data;
      this.emitErrorMessageEvent(transactionId);
    });
    this.fileDialogService.isLoadingChange.subscribe(data => {
      this.isLoading = data;
      this.emitLoadingEvent(transactionId);
    });
    this.fileDialogService.openFileDetailsDialog(file, true);
  }

  openTransactionDocumentInfo = (transaction: Transaction) => this.fileService.getDocumentContent(transaction.docID)
    .pipe(data => this.setLoading(data))
    .subscribe((data: DocumentContent) => {
      this.isLoading = false;
      this.emitLoadingEvent(transaction.id);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Primary Document`,
        tabs: getTransactionDocumentInfoTabs({ ...data, processID: transaction.workflowID }),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.emitLoadingEvent(transaction.id);
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

  emitErrorMessageEvent(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id].emit(this.errorMessage);
    }
  }

  emitLoadingEvent(id: number) {
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id].emit(this.isLoading);
    }
  }

  createEmitters(id: number) {
    this.errorMessageEmitters[id] = new EventEmitter<ErrorMessage>();
    this.isLoadingEmitters[id] = new EventEmitter<boolean>();
  }

  deleteEmitters(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id] = null;
    }
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id] = null;
    }
  }

}
