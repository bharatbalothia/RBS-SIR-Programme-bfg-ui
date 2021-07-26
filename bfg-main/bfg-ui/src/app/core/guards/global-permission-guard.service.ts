import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { ERROR_MESSAGES } from '../constants/error-messages';
import { GLOBAL_PERMISSIONS } from '../constants/global-permissions';

@Injectable({
    providedIn: 'root'
})
export class GlobalPermissionGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
        if (this.hasGlobalPermission()) {
            return true;
        } else {
            this.auth.showForbidden(ERROR_MESSAGES['SFG_UI_HOME']);
        }

    }

    hasGlobalPermission(): boolean {
        return this.auth.isEnoughPermissions([GLOBAL_PERMISSIONS.HOME]);
    }
}
