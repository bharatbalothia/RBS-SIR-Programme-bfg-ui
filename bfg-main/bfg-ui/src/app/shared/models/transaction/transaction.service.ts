import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TransactionsWithPagination } from './transactions-with-pagination.model';
import { TransactionCriteriaData } from './transaction-criteria.model';
import { Transaction } from './transaction.model';

@Injectable({
    providedIn: 'root'
})
export class TransactionService {

    private apiUrl: string = environment.apiUrl + 'transactions/';

    constructor(private http: HttpClient) {
    }

    getTransactionCriteriaData(params?: { outbound?, service?: string }) {
        return this.http.get<TransactionCriteriaData>(this.apiUrl + 'transaction-criteria-data', { params });
    }

    getTransactionList(params?): Observable<TransactionsWithPagination> {
        return this.http.post<TransactionsWithPagination>(this.apiUrl, params);
    }

    getTransactionById(id: number) {
        return this.http.get<Transaction>(this.apiUrl + id);
    }

}
