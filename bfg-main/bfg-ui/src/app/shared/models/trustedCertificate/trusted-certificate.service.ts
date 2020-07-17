import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { TrustedCertificate } from './trusted-certificate.model';
import { TrustedCertificatesWithPagination } from './trusted-certificates-with-pagination.model';
import { Observable } from 'rxjs';

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

}
