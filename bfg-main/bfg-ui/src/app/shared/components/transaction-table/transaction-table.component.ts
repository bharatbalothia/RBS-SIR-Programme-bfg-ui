import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { getDirectionIcon, getTransactionDetailsTabs, getTransactionSearchDisplayName } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { getBusinessProcessDisplayName } from '../../models/business-process/business-process-display-names';
import { FileDialogService } from '../../models/file/file-dialog.service';
import { Transaction } from '../../models/transaction/transaction.model';
import { TransactionService } from '../../models/transaction/transaction.service';
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
export class TransactionTableComponent implements OnInit {

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

  @Output() isLoadingChange: EventEmitter<boolean> = new EventEmitter<boolean>();

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
    private transactionService: TransactionService,
    private dialog: MatDialog,
    private fileDialogService: FileDialogService
  ) { }

  ngOnInit(): void {
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
    this.transactionService.getTransactionById(id)
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
          data,
          getTabs: () => getTransactionDetailsTabs(data, actions),
          displayName: getTransactionSearchDisplayName,
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
      getTabs: () => [],
      displayName: getBusinessProcessDisplayName,
      actionData: {
        id: transaction.workflowID,
        actions: {
        }
      },
    }))

}
