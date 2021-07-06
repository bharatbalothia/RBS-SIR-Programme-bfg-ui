import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { getApiErrorMessage } from 'src/app/core/utils/error-template';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { isHtml, isJson } from 'src/app/shared/utils/utils';
import { ApplicationDataService } from '../models/application-data/application-data.service';

@Injectable()
export class NotificationHttpInterceptor implements HttpInterceptor {
  constructor(
    public notificationService: NotificationService,
    private applicationDataService: ApplicationDataService
  ) { }

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
              else if (typeof error.error.text === 'string' && isHtml(error.error.text)) {
                this.showHtmlError(error.error.text);
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

  showHtmlError = (htmlError) => {
    const parsedError = new DOMParser().parseFromString(htmlError, 'text/html').getElementsByTagName("body")[0].firstChild.textContent;
    this.applicationDataService.f5Link.subscribe(data => {
      let htmlContent = null;
      if (data) {
        htmlContent = 'This is F5 related issue in testing environment. Please share the error details  to:';
        data.forEach(el => htmlContent += `</br><a href="${el.isMail ? 'mailto:' + el.link : el.link}">${el.link}</a>`);
      }
      this.notificationService.showErrorMessage({ code: null, message: parsedError, errors: htmlContent && [{ htmlContent }] }, true);
    });
  }

}
