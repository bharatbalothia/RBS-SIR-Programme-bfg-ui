import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { interval, Subscription } from 'rxjs';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { getDirectionIcon, getTransactionDetailsTabs, getTransactionSearchDisplayName } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { getBusinessProcessDisplayName } from '../../models/business-process/business-process-display-names';
import { FileDialogService } from '../../models/file/file-dialog.service';
import { FileService } from '../../models/file/file.service';
import { Transaction } from '../../models/transaction/transaction.model';
import { TransactionsWithPagination } from '../../models/transaction/transactions-with-pagination.model';
import { BusinessProcessDialogConfig } from '../business-process-dialog/business-process-dialog-config.model';
import { BusinessProcessDialogComponent } from '../business-process-dialog/business-process-dialog.component';
import { DetailsDialogConfig } from '../details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from '../details-dialog/details-dialog.component';

@Component({
  selector: 'app-transaction-table',
  templateUrl: './transaction-table.component.html',
  styleUrls: ['./transaction-table.component.scss']
})
export class TransactionTableComponent implements OnInit, OnDestroy {

  getTransactionStatusIcon = getStatusIcon;
  getDirectionIcon = getDirectionIcon;

  @Input() getTransactionList: (pageIndex: number, pageSize: number) => any;
  @Input() dataSource: MatTableDataSource<Transaction>;
  @Input() transactions: TransactionsWithPagination;
  @Input() isLoading = false;
  @Input() pageIndex;
  @Input() pageSize;
  @Input() shouldHidePaginator = false;
  @Input() shouldHideTableHeader = false;
  @Input() shouldAutoRefresh = true;

  @Output() isLoadingChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  autoRefreshing: Subscription;

  displayedColumns: string[] = [
    'id',
    'status',
    'transactionID',
    'type',
    'timestamp',
    'WFID',
    'settleDate',
    'settleAmount',
  ];

  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private fileService: FileService,
    private dialog: MatDialog,
    private fileDialogService: FileDialogService
  ) { }

  ngOnInit(): void {
    this.autoRefreshChange(this.shouldAutoRefresh);
  }

  setLoading(data) {
    this.isLoading = true;
    this.isLoadingChange.emit(true);
    return data;
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  openTransactionDetailsDialog = (fileId: number, id: number) => {
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        this.isLoadingChange.emit(false)
        const actions = {
          workflowID: () => this.openBusinessProcessDialog(data),
          file: () => this.openFileDetailsDialog({ id: fileId })
        };
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction - ${data.id}`,
          tabs: getTransactionDetailsTabs(data, actions),
          displayName: getTransactionSearchDisplayName,
          isDragable: true,
          actionData: {
            actions
          }
        }));
      },
        error => {
          this.isLoading = false;
          this.isLoadingChange.emit(false);
        });
  }

  openFileDetailsDialog = file => this.fileDialogService.openFileDetailsDialog(file, true);

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

  autoRefreshChange = (value) => {
    if (value) {
      this.autoRefreshing = interval(60000).subscribe(() => this.getTransactionList(this.pageIndex, this.pageSize));
    }
    else if (this.autoRefreshing) {
      this.autoRefreshing.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }

}
