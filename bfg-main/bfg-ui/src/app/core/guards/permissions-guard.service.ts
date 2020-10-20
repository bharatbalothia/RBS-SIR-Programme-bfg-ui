import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { GLOBAL_PERMISSIONS } from '../constants/global-permissions';
import { ROUTING_PATHS } from '../constants/routing-paths';


@Injectable({
    providedIn: 'root'
})
export class PermissionsGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
        if (this.hasGlobalPermission()) {
            const permit = this.auth.isEnoughPermissions(route.data.permissions);
            if (!permit) {
                this.auth.showForbidden();
            }
            return permit;
        } else {
            this.auth.showForbidden();
            return this.router.parseUrl('/' + ROUTING_PATHS.LOGIN);
        }

    }

    hasGlobalPermission(): boolean {
        return this.auth.isEnoughPermissions([GLOBAL_PERMISSIONS.HOME]);
    }
}
