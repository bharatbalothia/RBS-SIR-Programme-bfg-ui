import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SEPAFilesWithPagination } from './sepa-files-with-pagination.model';

@Injectable({
  providedIn: 'root'
})
export class SEPADashboardService {

  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getSEPAFileList(params?): Observable<SEPAFilesWithPagination> {
    return this.http.post<SEPAFilesWithPagination>(this.apiUrl + 'files/sepa', params);
  }
}