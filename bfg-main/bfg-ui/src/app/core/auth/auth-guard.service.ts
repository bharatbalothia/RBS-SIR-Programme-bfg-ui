import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from './auth.service';
import { ROUTING_PATHS } from '../constants/routing-paths';


@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(): boolean {
        if (!this.auth.isAuthenticated()) {
            this.router.navigate(['/' + ROUTING_PATHS.LOGIN]);
            return false;
        }
        return true;
    }
}
