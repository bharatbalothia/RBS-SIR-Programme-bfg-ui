import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { TrustedCertificate } from './trusted-certificate.model';
import { TrustedCertificatesWithPagination } from './trusted-certificates-with-pagination.model';
import { Observable } from 'rxjs';
import { ChangeControlsWithPagination } from '../changeControl/change-controls-with-pagination.model';
import { ChangeResolution } from '../changeControl/change-resolution.model';
import { ChangeControl } from '../changeControl/change-control.model';

@Injectable({
  providedIn: 'root'
})
export class TrustedCertificateService {

  private apiUrl: string = environment.apiUrl + 'certificates/';

  constructor(private http: HttpClient) {
  }

  getTrustedCertificateList(params?: {
    certName?: string;
    thumbprint?: string;
    thumbprint256?: string;
    page?: string;
    size?: string
  }): Observable<TrustedCertificatesWithPagination> {
    return this.http.get<TrustedCertificatesWithPagination>(this.apiUrl, { params });
  }

  uploadTrustedCertificate(formData) {
    return this.http.post<TrustedCertificate>(this.apiUrl + 'upload', formData);
  }

  createTrustedCertificate(formData) {
    return this.http.post<TrustedCertificate>(this.apiUrl, formData);
  }

  getPendingChanges(params?: { page?: string; size?: string }): Observable<ChangeControlsWithPagination> {
    return this.http.get<ChangeControlsWithPagination>(this.apiUrl + 'pending', { params });
  }

  getPendingChangeById(changeId) {
    return this.http.get<ChangeControl>(this.apiUrl + 'pending/' + changeId);
  }

  editPendingChange(changeId, params: { name: string, comments?: string }) {
    return this.http.put<ChangeControl>(this.apiUrl + 'pending/' + changeId, params);
  }

  getCertificateById(certificateId: string) {
    return this.http.get<TrustedCertificate>(this.apiUrl + certificateId);
  }

  validateCertificateById(certificateId: string) {
    return this.http.get<TrustedCertificate>(this.apiUrl + 'validate/' + certificateId);
  }

  resolveChange(resolution: ChangeResolution) {
    return this.http.post(this.apiUrl + 'pending', resolution);
  }

  deleteTrustedCertificate(certificateId: string, changerComments: string) {
    return this.http.delete(this.apiUrl + certificateId, { params: changerComments && { changerComments } });
  }

  isTrustedCertificateNameExists(name: string): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence', {
      params: {
        name,
      }
    });
  }

}
