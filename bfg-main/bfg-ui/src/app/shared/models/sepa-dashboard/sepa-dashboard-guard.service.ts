import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ApplicationDataService } from '../application-data/application-data.service';

@Injectable({
    providedIn: 'root'
})
export class SEPADashboardGuardService implements CanActivate {

    sepaDashboardVisibility: boolean;

    constructor(private applicationDataService: ApplicationDataService, public router: Router) {
    }

    canActivate(): boolean | Promise<boolean | UrlTree> {
        return this.applicationDataService.isSepaDashboardVisible().toPromise().then(data => {
            this.applicationDataService.applicationData
                .next({ ...this.applicationDataService.applicationData.value, sepaDashboardVisibility: data });
            if (data) {
                return true;
            } else {
                this.router.navigate(['/' + ROUTING_PATHS.HOME]);
            }
        });
    }
}
