import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ROUTING_PATHS } from '../constants/routing-paths';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(public router: Router) { }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        return next.handle(req)
            .pipe(
                catchError((error: any) => {
                    if (error instanceof HttpErrorResponse) {
                        if (error.status === 401) {
                            this.router.navigate(['/' + ROUTING_PATHS.LOGIN]);
                        }
                    }
                    return throwError(error);
                }));
    }
}
