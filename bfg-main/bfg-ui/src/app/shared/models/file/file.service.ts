<<<<<<< HEAD
import { environment } from 'src/environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
=======
import { HttpClient } from '@angular/common/http';
>>>>>>> develop
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { removeEmpties } from '../../utils/utils';
import { TransactionsWithPagination } from '../transaction/transactions-with-pagination.model';
import { FileCriteriaData } from './file-criteria.model';
import { FileError } from './file-error.model';
import { FilesWithPagination } from './files-with-pagination.model';

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

    exportTransactionsReport(fileId, params?: { fileName, size, type }): Observable<HttpResponse<Blob>> {
        return this.http.get<Blob>(this.apiUrl + fileId + '/transactions/export-report', {
            observe: 'response',
            responseType: 'blob' as 'json',
            params
        });
    }
}
