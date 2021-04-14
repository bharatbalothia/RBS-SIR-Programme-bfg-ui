import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';
import { ROUTING_PATHS } from '../constants/routing-paths';
import { take } from 'rxjs/operators';
import { defineReferrer } from 'src/app/shared/utils/utils';


@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean | Promise<boolean | UrlTree> {
        if (this.auth.isAuthenticated()) {
            return true;
        } else {
            return new Promise((resolve) => {
                if (this.auth.isValidSSOLink(route.queryParams)) {
                    this.auth.ssoLogIn(this.auth.extractSSOCredentials(route.queryParams)).pipe(take(1)).subscribe(
                        () => {
                            resolve(true);
                        },
                        () => {
                            defineReferrer(window.location.href);
                            resolve(this.router.parseUrl('/' + ROUTING_PATHS.LOGIN));
                        }
                    );
                } else {
                    defineReferrer(window.location.href);
                    resolve(this.router.parseUrl('/' + ROUTING_PATHS.LOGIN));
                }
            });

        }
    }

}
