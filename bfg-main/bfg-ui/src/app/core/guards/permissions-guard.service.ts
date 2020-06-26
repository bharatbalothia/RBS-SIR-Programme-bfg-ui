import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { ROUTING_PATHS } from '../constants/routing-paths';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';


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
