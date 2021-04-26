import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { get } from 'lodash';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { getEntityDisplayName, getPendingChangesTabs } from 'src/app/features/setup/entities/entity-display-names';
import { getTrustedCertificateDisplayHeader, getTrustedCertificatePendingChangesTabs } from 'src/app/features/setup/trusted-certificates/trusted-certificate-display-names';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { AuditEvent } from 'src/app/shared/models/audit-event/audit-event.model';
import { AUDIT_EVENT_TYPES, AUDIT_EVENT_ACTIONS } from 'src/app/shared/models/audit-event/audit-events-constants';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { NotificationService } from 'src/app/shared/services/notification.service';

@Component({
  selector: 'app-home-event',
  templateUrl: './home-event.component.html',
  styleUrls: ['./home-event.component.scss']
})
export class HomeEventComponent implements OnInit {

  AUDIT_EVENT_TYPES = AUDIT_EVENT_TYPES;
  AUDIT_EVENT_ACTIONS = AUDIT_EVENT_ACTIONS;

  @Input() event: AuditEvent;
  @Input() isLoading = false;

  @Output() isLoadingChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(
    private authService: AuthService,
    private entityService: EntityService,
    private trustedCertificateService: TrustedCertificateService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
  }

  openEntityApprovingDialog(changeID: string) {
    const getChangeControl = () => this.getPendingEntityDetails(changeID)
      .then((detailedChangeCtrl: ChangeControl) =>
        detailedChangeCtrl && this.addEntityBeforeToChangeControl(detailedChangeCtrl));

    getChangeControl().then((changeCtrl: ChangeControl) => changeCtrl &&
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        yesCaption: 'Cancel',
        getData: getChangeControl,
        data: changeCtrl,
        getTabs: (data: ChangeControl) => getPendingChangesTabs(data, true),
        displayName: getEntityDisplayName,
        actionData: {
          changeID: changeCtrl.changeID,
          changer: changeCtrl.changer,
          errorMessage: {
            message: null,
            errors: get(changeCtrl, 'errors'),
          },
          warningMessage: this.authService.isTheSameUser(changeCtrl.changer) && ERROR_MESSAGES.approvingChanges,
          shouldDisableApprove: this.authService.isTheSameUser(changeCtrl.changer),
          approveAction:
            (params: { changeID: string, status: string, approverComments: string }) => this.entityService.resolveChange(params)
        }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.notificationService.show(
            `Entity ${get(data, 'status').toLowerCase()}`,
            `Entity ${changeCtrl.entityLog.entity} has been ${get(data, 'status').toLowerCase()}`,
            'success'
          );
        }
      }));
  }

  addEntityBeforeToChangeControl(changeControl: ChangeControl) {
    const entityId = get(changeControl.entityLog, 'entityId');
    if (entityId) {
      this.isLoadingChange.emit(true);
      return this.entityService.getEntityById(entityId.toString()).toPromise()
        .then(data => {
          this.isLoadingChange.emit(false);
          return ({ ...changeControl, entityBefore: data });
        }).finally(() => {
          this.isLoadingChange.emit(false);
        });
    }
    else {
      return new Promise((res) => res(changeControl));
    }
  }

  getPendingEntityDetails = (changeID: string) => {
    this.isLoadingChange.emit(true);
    return this.entityService.getPendingChangeById(changeID).toPromise()
      .finally(() => {
        this.isLoadingChange.emit(false);
      });
  }


  openTrustedCertificateApprovingDialog(changeID: string) {
    const getValidatedChangeControl = () => this.getChangeControlDetails(changeID)
      .then((chngCtl: ChangeControl) => this.addCertificateBeforeToChangeControl(chngCtl)
        .then(changeCtrl => this.addValidationToChangeControl(changeCtrl)));

    getValidatedChangeControl().then(validatedChangeControl =>
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        data: validatedChangeControl,
        getData: getValidatedChangeControl,
        getTabs: (data) => getTrustedCertificatePendingChangesTabs(data, true),
        yesCaption: 'Cancel',
        displayName: getTrustedCertificateDisplayHeader,
        actionData: {
          changeID: validatedChangeControl.changeID,
          changer: validatedChangeControl.changer,
          errorMessage: {
            message: (get(validatedChangeControl, 'errors') && ERROR_MESSAGES['trustedCertificateErrors']),
            warnings: get(validatedChangeControl, 'warnings'),
            errors: get(validatedChangeControl, 'errors')
          },
          warningMessage: this.authService.isTheSameUser(validatedChangeControl.changer) && ERROR_MESSAGES.approvingChanges,
          shouldDisableApprove: this.authService.isTheSameUser(validatedChangeControl.changer),
          approveAction:
            (params: { changeID: string, status: string, approverComments: string }) =>
              this.trustedCertificateService.resolveChange(params)
        }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.notificationService.show(
            `Trusted Certificate ${get(data, 'status').toLowerCase()}`,
            `Trusted Certificate ${validatedChangeControl.trustedCertificateLog.certificateName} has been ${get(data, 'status').toLowerCase()}`,
            'success'
          );
        }
      }));
  }

  getChangeControlDetails = (changeID: string) => {
    this.isLoadingChange.emit(true);
    return this.trustedCertificateService.getPendingChangeById(changeID).toPromise()
      .finally(() => {
        this.isLoadingChange.emit(false);
      });
  }

  addCertificateBeforeToChangeControl = (changeControl: ChangeControl): Promise<any> =>
    new Promise(resolve =>
      resolve(changeControl.trustedCertificateLog && { ...changeControl, certificateBefore: changeControl.trustedCertificateLog }))

  addValidationToChangeControl(changeControl: ChangeControl): Promise<any> {
    const certificateLogId = get(changeControl.trustedCertificateLog, 'certificateLogId');
    this.isLoadingChange.emit(true);
    return this.trustedCertificateService.validateCertificateLogById(certificateLogId.toString()).toPromise()
      .then(data => {
        return ({
          ...changeControl,
          trustedCertificateLog: {
            ...changeControl.trustedCertificateLog,
            authChainReport: data.authChainReport,
            valid: data.valid
          },
          certificateBefore: changeControl.certificateBefore && {
            ...changeControl.certificateBefore,
            authChainReport: data.authChainReport,
            valid: data.valid
          },
          warnings: data.certificateWarnings,
          errors: data.certificateErrors,
        });
      }).finally(() => this.isLoadingChange.emit(false));
  }
}
