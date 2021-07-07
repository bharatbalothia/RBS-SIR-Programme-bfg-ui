import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApplicationData } from './application-data.model';
import { F5Link } from './f5-link.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationDataService {

  private apiUrl: string = environment.apiUrl + 'version/';

  applicationData: BehaviorSubject<ApplicationData> = new BehaviorSubject<ApplicationData>
    ({ version: '', loginText: '', sepaDashboardVisibility: null });

  f5Link: BehaviorSubject<F5Link[]> = new BehaviorSubject<F5Link[]>(null);

  constructor(private http: HttpClient) {
  }

  getApplicationVersion() {
    this.http.get<ApplicationData>(this.apiUrl).subscribe(data => this.applicationData.next(data));
  }
<<<<<<< HEAD
=======

  getF5Link() {
    this.http.get<F5Link[]>(this.apiUrl + 'f5').subscribe(data => this.f5Link.next(data));
  }
>>>>>>> develop-SEPA
}
