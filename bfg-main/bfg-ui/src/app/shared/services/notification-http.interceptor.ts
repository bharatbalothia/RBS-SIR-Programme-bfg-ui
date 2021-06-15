import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { getApiErrorMessage } from 'src/app/core/utils/error-template';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { isHtml, isJson } from 'src/app/shared/utils/utils';

@Injectable()
export class NotificationHttpInterceptor implements HttpInterceptor {
  constructor(public notificationService: NotificationService) { }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        catchError((error: any) => {
          if (error instanceof HttpErrorResponse) {
            try {
              if (typeof error.error === 'string' && isJson(error.error)) {
                this.notificationService.showErrorMessage(getApiErrorMessage(JSON.parse(error.error as string)));
              }
              else if (typeof error.error === 'string' && isHtml(error.error)) {
                this.notificationService.showErrorMessage({ code: null, message: this.parseHtmlError(error.error) }, true);
              }
              else {
                this.notificationService.showErrorMessage(getApiErrorMessage(error));
              }
            } catch (e) {
              this.notificationService.showErrorMessage({
                code: `${error.status}`,
                message: 'An error occurred while processing request'
              });
            }
          }
          return throwError(error);
        }));
  }

  parseHtmlError = (htmlError) => new DOMParser().parseFromString(htmlError, 'text/html').getElementsByTagName("body")[0].firstChild.textContent

}
