import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { take } from 'rxjs/operators';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { TransactionService } from 'src/app/shared/models/transaction/transaction.service';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';

@Component({
  selector: 'app-transaction-monitor',
  templateUrl: './transaction-monitor.component.html',
  styleUrls: ['./transaction-monitor.component.scss']
})
export class TransactionMonitorComponent implements OnInit {

  errorMessage: ErrorMessage;
  isLoading = false;

  transactions: TransactionsWithPagination;
  dataSource: MatTableDataSource<Transaction>;
  pageIndex = 0;
  pageSize = 100;

  URLParams;

  constructor(
    private transactionService: TransactionService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.URLParams = params;
      this.getTransactionList(this.pageIndex, this.pageSize);
    });
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }


  getTransactionList(pageIndex: number, pageSize: number) {
    const formData = {
      page: pageIndex.toString(),
      size: pageSize.toString(),
      ...this.URLParams
    };

    this.transactionService.getTransactionList(formData)
      .pipe(data => this.setLoading(data))
      .pipe(take(1)).subscribe((data: TransactionsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.transactions = data;
        this.updateTable();
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

}
