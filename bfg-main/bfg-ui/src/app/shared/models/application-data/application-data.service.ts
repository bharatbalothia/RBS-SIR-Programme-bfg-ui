import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApplicationData } from './application-data.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationDataService {

  private apiUrl: string = environment.apiUrl + 'version/';

  applicationData: BehaviorSubject<ApplicationData> = new BehaviorSubject<ApplicationData>({ version: '', loginText: '' });

  constructor(private http: HttpClient) {
  }

  getApplicationVersion() {
    this.http.get<ApplicationData>(this.apiUrl).subscribe(data => this.applicationData.next(data));
  }
}