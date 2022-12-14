import { Injectable } from '@angular/core';
import { IndividualConfig, ToastrService } from 'ngx-toastr';
import { ErrorMessage, ErrorsField, getErrorsMessage } from 'src/app/core/utils/error-template';

type NotificationType = 'success' | 'error' | 'info' | 'warning';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private toasterConfig: IndividualConfig;

  constructor(private toastrService: ToastrService) {
    this.toasterConfig = {
      ...toastrService.toastrConfig,
      closeButton: true,
      timeOut: 10 * 1000,
      extendedTimeOut: 10 * 1000,
      enableHtml: true,
      progressBar: true
    };
  }

  public show(title: string, message: string, type: NotificationType) {
    return this.toastrService.show(message, title, this.toasterConfig, `toast-${type}`);
  }

  public showErrorMessage(errorMessage: ErrorMessage, shouldDisableTimer?: boolean) {
    if (errorMessage.message || errorMessage.errors) {
      const title = `${errorMessage.message || ''} ${errorMessage.code ? `(${errorMessage.code})` : ''}`;
      const message = this.composeHtmlBody(errorMessage.errors);
      if (shouldDisableTimer) {
        this.toasterConfig.disableTimeOut = true;
      }
      this.toastrService.error(message, title, { ...this.toasterConfig, disableTimeOut: shouldDisableTimer, tapToDismiss: !shouldDisableTimer });
    }
  }

  public showWarningMessage(errorMessage: ErrorMessage) {
    if (errorMessage.warnings) {
      const title = `Warnings: `;
      const message = this.composeHtmlBody(errorMessage.warnings);

      this.toastrService.warning(message, title, this.toasterConfig);
    }
  }

  public showWarningText(text: string) {
    this.toastrService.warning(text, '', this.toasterConfig);
  }

  public showErrorWithWarningMessage(errorMessage: ErrorMessage) {
    this.showErrorMessage(errorMessage);
    this.showWarningMessage(errorMessage);
  }

  private composeHtmlBody(fields: ErrorsField[]) {
    let message = '';

    if (fields && fields.length > 0) {
      message += '<ul>';
      fields.forEach(error => message += `<li>${getErrorsMessage(error)}</li>`);
      message += '</ul>';
    }

    return message;
  }
}
