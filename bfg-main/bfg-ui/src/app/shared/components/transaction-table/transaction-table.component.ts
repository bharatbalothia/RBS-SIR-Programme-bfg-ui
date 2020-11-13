import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { ErrorMessage } from 'src/app/core/utils/error-template';
import { Transaction } from '../../models/transaction/transaction.model';
import { TransactionsWithPagination } from '../../models/transaction/transactions-with-pagination.model';

@Component({
  selector: 'app-transaction-table',
  templateUrl: './transaction-table.component.html',
  styleUrls: ['./transaction-table.component.scss']
})
export class TransactionTableComponent implements OnInit {

  getTransactionStatusIcon = getStatusIcon;

  @Input() getTransactionList: (pageIndex: number, pageSize: number) => any;
  @Input() errorMessage: ErrorMessage;
  @Input() dataSource: MatTableDataSource<Transaction>;
  @Input() Transactions: TransactionsWithPagination;
  @Input() isLoading = false;
  @Input() pageIndex;
  @Input() pageSize;
  @Input() shouldHidePaginator = false;
  @Input() shouldHideTableHeader = false;
  @Input() shouldAutoRefresh = true;

  errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};

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

  constructor() { }

  ngOnInit(): void {
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

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
