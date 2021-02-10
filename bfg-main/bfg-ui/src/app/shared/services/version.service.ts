import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VersionService {

  private apiUrl: string = environment.apiUrl + 'version/';

  applicationVersion: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  constructor(private http: HttpClient) {
  }

  getApplicationVersion() {
    this.http.get(this.apiUrl, { responseType: 'text' }).subscribe(data => this.applicationVersion.next(data));
  }
}
