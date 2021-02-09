import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VersionService {

  private apiUrl: string = environment.apiUrl + 'auth/version/';

  applicationVersion: Subject<string> = new Subject<string>();

  constructor(private http: HttpClient) {
  }

  getApplicationVersion() {
    this.http.get(this.apiUrl, {
      responseType: 'text',
      headers: {
        accept: '*/*'
      }
    }).subscribe(data => this.applicationVersion.next(data));
  }
}
