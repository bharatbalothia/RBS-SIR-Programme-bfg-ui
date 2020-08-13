import { Component, OnInit, Inject } from '@angular/core';
import { TransactionsWithPagination } from 'src/app/shared/models/file/transactions-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { Transaction } from 'src/app/shared/models/file/transaction.model';
import { FileService } from 'src/app/shared/models/file/file.service';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { get } from 'lodash';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { getFileSearchDisplayName, getTransactionDetailsTabs } from '../file-search-display-names';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';

@Component({
  selector: 'app-transactions-dialog',
  templateUrl: './transactions-dialog.component.html',
  styleUrls: ['./transactions-dialog.component.scss']
})
export class TransactionsDialogComponent implements OnInit {

  getFileSearchDisplayName = getFileSearchDisplayName;

  displayName: (fieldName: string) => string;

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
    this.isLoading = true;
    this.fileService.getTransactionListByFileId(this.fileId, { page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1)).subscribe((data: TransactionsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.transactions = data;
        this.updateTable();
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

  openTransactionDetailsDialog = (fileId: number, transactionId: number) => this.fileService.getTransactionById(fileId, transactionId)
    .pipe(data => this.setLoading(data))
    .subscribe((data: Transaction) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Transaction Details of ${data.transactionID}`,
        tabs: getTransactionDetailsTabs(data),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      })

}
