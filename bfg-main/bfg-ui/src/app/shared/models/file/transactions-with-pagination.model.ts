import { Transaction } from './transaction.model';

export interface TransactionsWithPagination {
    content: [
        Transaction
    ];
    pageable: {
        pageSize: number;
        pageNumber: number;
        offset: number;
    };
    size: number;
    totalElements: number;
    totalPages: number;
}
