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
              if (error.error instanceof Blob) {
                this.interceptBlobError(error);
              }
              else if (typeof error.error === 'string' && isJson(error.error)) {
                this.notificationService.showErrorMessage(getApiErrorMessage(JSON.parse(error.error as string)));
              }
              else if (typeof error.error === 'string' && isHtml(error.error)) {
                this.showHtmlError(error.error);
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

  interceptBlobError = (error: HttpErrorResponse) => {
    const reader: FileReader = new FileReader();
    reader.onloadend = () => this.notificationService.showErrorMessage(getApiErrorMessage(JSON.parse(reader.result as string)));
    reader.readAsText(error.error);
  }

  showHtmlError = (htmlError) => {
    const parsedError = new DOMParser().parseFromString(htmlError, 'text/html').getElementsByTagName("body")[0].firstChild.textContent;
    const htmlContent = 'This is F5 related issue in testing environment. Please share the error details  to:' +
      '</br><a href="mailto:Sriram.jayaraman@natwest.com">Sriram.jayaraman@natwest.com</a>' +
      '</br><a href="mailto:Manoj.Bansal@natwest.com">Manoj.Bansal@natwest.com</a>' +
      '</br><a href="mailto:Maryia.Sukhaparava@natwest.com">Maryia.Sukhaparava@natwest.com</a>' +
      '</br><a href="mailto:arokia.williamvijay@rbs.co.uk">arokia.williamvijay@rbs.co.uk</a>'
    this.notificationService.showErrorMessage({ code: null, message: parsedError, errors: [{ htmlContent }] }, true);
  }

}
