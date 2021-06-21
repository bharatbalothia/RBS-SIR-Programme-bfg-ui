import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FileCriteriaData } from './file-criteria.model';
import { Observable } from 'rxjs';
import { FilesWithPagination } from './files-with-pagination.model';
import { TransactionsWithPagination } from '../transaction/transactions-with-pagination.model';
import { FileError } from './file-error.model';
import { Transaction } from '../transaction/transaction.model';
import { removeEmpties } from '../../utils/utils';

@Injectable({
    providedIn: 'root'
})
export class FileService {

    private apiUrl: string = environment.apiUrl + 'files/';

    constructor(private http: HttpClient) {
    }

    getFileCriteriaData(params?: { outbound?, service?: string }) {
        return this.http.get<FileCriteriaData>(this.apiUrl + 'file-criteria-data', { params });
    }

    getFileList(params?): Observable<FilesWithPagination> {
        return this.http.post<FilesWithPagination>(this.apiUrl, params);
    }

    getFileById(fileId: number) {
        return this.http.get<File>(this.apiUrl + fileId);
    }

    getTransactionListByFileId(fileId: number, params?: { page?: string, size?: string }) {
        return this.http.get<TransactionsWithPagination>(this.apiUrl + fileId + '/transactions', { params });
    }

    getTransactionById(fileId: number, id: number) {
        return this.http.get<Transaction>(this.apiUrl + fileId + '/transactions/' + id);
    }

    getErrorDetailsByCode(errorCode: string) {
        return this.http.get<FileError>(this.apiUrl + 'error/' + errorCode);
    }

    getDocumentContent(id, messageId?) {
        const params = removeEmpties({ id, messageId: id ? null : messageId });
        return this.http.get<FileError>(this.apiUrl + 'document-content/', { params });
    }

    getFileMonitorList() {
        return this.http.get<File[]>(this.apiUrl + 'file-monitor');
    }
}
