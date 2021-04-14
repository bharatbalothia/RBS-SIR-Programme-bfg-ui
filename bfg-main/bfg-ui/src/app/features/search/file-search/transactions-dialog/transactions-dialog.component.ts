import { Component, OnInit, Inject } from '@angular/core';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { FileService } from 'src/app/shared/models/file/file.service';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { get } from 'lodash';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { getDirectionIcon, getTransactionDetailsTabs, getTransactionDocumentInfoTabs } from '../../transaction-search/transaction-search-display-names';
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
  getDirectionIcon = getDirectionIcon;

  displayName: (fieldName: string) => string;

  isLoading = true;
  transactions: TransactionsWithPagination;
  displayedColumns: string[] = ['id', 'transactionID', 'settleDate', 'settleAmount', 'type', 'status', 'workflowID'];
  dataSource: MatTableDataSource<Transaction>;

  pageIndex = 0;
  pageSize = 50;
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
        error => this.isLoading = false
      );
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  openTransactionDetailsDialog = (fileId: number, id: number) => {
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        const actions: any = {
          file: () => this.openFileDetailsDialog({ id: fileId }),
          workflowID: () => this.openBusinessProcessDialog(data)
        };
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction -  ${data.id}`,
          data,
          getTabs: () => getTransactionDetailsTabs(data, ...actions),
          displayName: getFileSearchDisplayName,
          actionData: {
            actions
          },
        }));
      },
        error => this.isLoading = false
      );
  }

  openFileDetailsDialog = file => {
    this.fileDialogService.openFileDetailsDialog(file, true);
  }

  openTransactionDocumentInfo = (transaction: Transaction) => this.fileService.getDocumentContent(transaction.docID)
    .pipe(data => this.setLoading(data))
    .subscribe((docCont: DocumentContent) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        getTitle: (data) => data.document ? transaction.docID : `Primary Document`,
        data: docCont,
        getTabs: (data) => getTransactionDocumentInfoTabs({ ...data, processID: transaction.workflowID }),
        displayName: getFileSearchDisplayName,
        actionData: {
          actions: {
            processID: () => this.openBusinessProcessDialog(transaction)
          }
        },
      }));
    },
      error => this.isLoading = false
    )

  openBusinessProcessDialog = (transaction: Transaction) =>
    this.dialog.open(BusinessProcessDialogComponent, new BusinessProcessDialogConfig({
      title: `Business Process Detail`,
      getTabs: () => [],
      displayName: getBusinessProcessDisplayName,
      actionData: {
        id: transaction.workflowID,
        actions: {
        }
      },
    }))
}
