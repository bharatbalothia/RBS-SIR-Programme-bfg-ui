import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, UrlTree, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { ROUTING_PATHS } from '../constants/routing-paths';
import { take } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean | UrlTree> {
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
                            resolve(this.router.navigate(['/' + ROUTING_PATHS.LOGIN], { queryParams: { returnUrl: state.url } }));
                            return false;
                        }
                    );
                } else {
                    resolve(this.router.navigate(['/' + ROUTING_PATHS.LOGIN], { queryParams: { returnUrl: state.url } }));
                    return false;

                }
            });

        }
    }

}
