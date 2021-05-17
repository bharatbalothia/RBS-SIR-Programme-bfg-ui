import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Alerts } from './alerts.model';
import { SCTTraffic } from './sct-traffic.model';
import { SEPATraffic } from './sepa-traffic.model';
import { SystemErrors } from './system-errors.model';

@Injectable({
    providedIn: 'root'
})
export class StatisticsService {

    private apiUrl: string = environment.apiUrl + 'statistics/';

    constructor(private http: HttpClient) {
    }

    getAlerts() {
        return this.http.get<Alerts>(this.apiUrl + 'sct-alerts');
    }

    getSystemErrors() {
        return this.http.get<SystemErrors>(this.apiUrl + 'system-errors');
    }

    getSCTTraffic() {
        return this.http.get<SCTTraffic>(this.apiUrl + 'sct-traffic');
    }

    getSEPATraffic() {
        return this.http.get<SEPATraffic>(this.apiUrl + 'sepa-traffic');
    }
}
