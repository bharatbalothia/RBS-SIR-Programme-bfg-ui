import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ApplicationDataService } from '../application-data/application-data.service';

@Injectable({
    providedIn: 'root'
})
export class SEPADashboardGuardService implements CanActivate {

    sepaDashboardVisibility: boolean;

    constructor(private applicationDataService: ApplicationDataService, public router: Router) {
        this.applicationDataService.applicationData.subscribe(data => this.sepaDashboardVisibility = data.sepaDashboardVisibility);
    }

    canActivate(): boolean | Promise<boolean | UrlTree> {
        if (this.sepaDashboardVisibility) {
            return true;
        } else {
            this.router.navigate(['/' + ROUTING_PATHS.HOME]);
        }
    }

}
