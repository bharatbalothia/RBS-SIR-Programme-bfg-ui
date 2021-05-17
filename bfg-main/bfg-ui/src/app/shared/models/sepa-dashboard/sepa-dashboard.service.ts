import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SEPAFilesWithPagination } from './sepa-files-with-pagination.model';

@Injectable({
  providedIn: 'root'
})
export class SEPADashboardService {

  private apiUrl: string = environment.apiUrl + 'files/sepa';

  constructor(private http: HttpClient) {
  }

  getSEPAFileList(params?): Observable<SEPAFilesWithPagination> {
    return this.http.post<SEPAFilesWithPagination>(this.apiUrl, params);
  }


  exportExcel(params?: { from?: string, to?: string }): Observable<HttpResponse<Blob>> {
    return this.http.get<Blob>(this.apiUrl + '/export-excel', {
      observe: 'response',
      responseType: 'blob' as 'json',
      params
    });
  }

}
