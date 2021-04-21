import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AuditEvent } from './audit-event.model';
import { AuditEventCriteria } from './audit-event-criteria.model';

@Injectable({
  providedIn: 'root'
})
export class AuditEventService {

  private apiUrl: string = environment.apiUrl + 'news/';

  constructor(private http: HttpClient) {
  }

  getAuditEvents(params: AuditEventCriteria) {
    return this.http.post<AuditEvent[]>(this.apiUrl, params);
  }

  getEventCriteriaData() {
    return this.http.get<AuditEventCriteria>(this.apiUrl + 'event-criteria-data');
  }

}
