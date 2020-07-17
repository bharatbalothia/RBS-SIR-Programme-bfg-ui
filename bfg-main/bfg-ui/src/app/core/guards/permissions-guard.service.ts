import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../auth/auth.service';


@Injectable({
    providedIn: 'root'
})
export class PermissionsGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const permit = this.auth.isEnoughPermissions(route.data.permissions);
        if (!permit) {
            this.auth.showForbidden();
        }
        return permit;
    }
}
