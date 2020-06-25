import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { TrustedCertificate } from './trusted-certificate.model';

@Injectable({
  providedIn: 'root'
})
export class TrustedCertificateServiceService {

  private apiUrl: string = environment.apiUrl + 'certificates/';

  constructor(private http: HttpClient) {
  }

  uploadTrustedCertificate(formData) {
    return this.http.post<TrustedCertificate>(this.apiUrl + 'upload', formData);
  }

}
