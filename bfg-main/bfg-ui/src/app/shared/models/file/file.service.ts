import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FileCriteriaData } from './file-criteria.model';
import { Observable } from 'rxjs';
import { FilesWithPagination } from './files-with-pagination.model';

@Injectable({
    providedIn: 'root'
})
export class FileService {

    private apiUrl: string = environment.apiUrl + 'files/';

    constructor(private http: HttpClient) {
    }

    getFileCriteriaData() {
        return this.http.get<FileCriteriaData>(this.apiUrl + 'file-criteria-data');
    }

    getFileList(params?): Observable<FilesWithPagination> {
        return this.http.get<FilesWithPagination>(this.apiUrl, { params });
    }

}
