import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { getApiErrorMessage } from 'src/app/core/utils/error-template';
import { NotificationService } from 'src/app/shared/services/notification.service';

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
}
