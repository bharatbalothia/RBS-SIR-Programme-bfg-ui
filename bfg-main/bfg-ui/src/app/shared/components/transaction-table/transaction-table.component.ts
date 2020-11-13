import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { interval, Subscription } from 'rxjs';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { getTransactionDetailsTabs, getTransactionSearchDisplayName } from 'src/app/features/search/transaction-search/transaction-search-display-names';
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

  @Input() getTransactionList: (pageIndex: number, pageSize: number) => any;
  @Input() errorMessage: ErrorMessage;
  @Input() dataSource: MatTableDataSource<Transaction>;
  @Input() transactions: TransactionsWithPagination;
  @Input() isLoading = false;
  @Input() pageIndex;
  @Input() pageSize;
  @Input() shouldHidePaginator = false;
  @Input() shouldHideTableHeader = false;
  @Input() shouldAutoRefresh = true;

  errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};
  isLoadingEmitters: { [id: number]: EventEmitter<boolean> } = {};

  @Output() errorMessageChange: EventEmitter<ErrorMessage> = new EventEmitter<ErrorMessage>();
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
    this.errorMessage = null;
    this.errorMessageChange.emit(null);
    this.isLoading = true;
    this.isLoadingChange.emit(true);
    return data;
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

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

  openTransactionDetailsDialog = (fileId: number, id: number) => {
    this.createEmitters(id);
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        this.isLoadingChange.emit(false)
        const actions = {
          workflowID: () => this.openBusinessProcessDialog(data),
          file: () => this.openFileDetailsDialog({ id: fileId }, id)
        };
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction - ${data.id}`,
          tabs: getTransactionDetailsTabs(data, actions),
          displayName: getTransactionSearchDisplayName,
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
          this.isLoadingChange.emit(false)
          this.errorMessage = getApiErrorMessage(error);
          this.errorMessageChange.emit(this.errorMessage);
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

  getDirectionIcon(direction: string) {
    return direction === 'outbound' ? 'call_made' : direction === 'inbound' ? 'call_received' : 'local_parking';
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }

}
