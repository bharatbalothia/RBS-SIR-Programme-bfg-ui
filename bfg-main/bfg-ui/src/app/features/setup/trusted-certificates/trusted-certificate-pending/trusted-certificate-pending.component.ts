import { Component, OnInit } from '@angular/core';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { getTrustedCertificateDetailsTabs, getTrustedCertificateDisplayName, getTrustedCertificatePendingChangesTabs } from '../trusted-certificate-display-names';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { MatTableDataSource } from '@angular/material/table';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { MatDialog } from '@angular/material/dialog';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { ChangeControlsWithPagination } from 'src/app/shared/models/changeControl/change-controls-with-pagination.model';
import { take } from 'rxjs/operators';
import { get } from 'lodash';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { Router } from '@angular/router';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';

@Component({
  selector: 'app-trusted-certificate-pending',
  templateUrl: './trusted-certificate-pending.component.html',
  styleUrls: ['./trusted-certificate-pending.component.scss']
})
export class TrustedCertificatePendingComponent implements OnInit {

  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;
  CHANGE_OPERATION = CHANGE_OPERATION;

  isLoading = true;
  isLoadingDetails = false;
  errorMessage: ErrorMessage;

  changeControls: ChangeControlsWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'name', 'thumbprint', 'thumbprint256'];
  dataSource: MatTableDataSource<ChangeControl>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private trustedCertificateService: TrustedCertificateService,
    private authService: AuthService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit() {
    this.getPendingChanges(this.pageIndex, this.pageSize);
  }

  getPendingChanges(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.trustedCertificateService.getPendingChanges({ page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1)).subscribe((data: ChangeControlsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.changeControls = data;
        this.updateTable();
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.changeControls.content);
  }

  addValidationToCertificate(trustedCertificate: TrustedCertificate): Promise<any> {
    const promise = new Promise((resolve) => {
      const certificateId = get(trustedCertificate, 'certificateId');
      const certificateLogId = get(trustedCertificate, 'certificateLogId');
      if (certificateId || certificateLogId) {
        this.isLoadingDetails = true;
        this.trustedCertificateService.validateCertificateById(certificateId || certificateLogId).toPromise()
          .then(data => {
            this.isLoadingDetails = false;
            resolve({
              ...trustedCertificate,
              authChainReport: data.authChainReport,
              valid: data.valid,
              warnings: data.certificateWarnings,
              errors: data.certificateErrors,
            });
          },
            error => {
              this.isLoadingDetails = false;
              this.errorMessage = getApiErrorMessage(error);
            });
      }
      else {
        resolve(trustedCertificate);
      }
    });
    return promise;
  }

  addValidationToChangeControl(changeControl: ChangeControl): Promise<any> {
    const promise = new Promise((resolve) => {
      const certificateLogId = get(changeControl.trustedCertificateLog, 'certificateLogId');
      if (certificateLogId) {
        this.isLoadingDetails = true;
        this.trustedCertificateService.validateCertificateById(certificateLogId.toString()).toPromise()
          .then(data => {
            this.isLoadingDetails = false;
            resolve({
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
          },
            error => {
              this.isLoadingDetails = false;
              this.errorMessage = getApiErrorMessage(error);
            });
      }
      else {
        resolve(changeControl);
      }
    });
    return promise;
  }

  addCertificateBeforeToChangeControl(changeControl: ChangeControl): Promise<any> {
    const promise = new Promise((resolve) => {
      const certificateId = get(changeControl.trustedCertificateLog, 'certificateId');
      if (certificateId) {
        this.isLoadingDetails = true;
        this.trustedCertificateService.getCertificateById(certificateId.toString()).toPromise()
          .then(data => {
            this.isLoadingDetails = false;
            resolve({ ...changeControl, certificateBefore: data });
          },
            error => {
              this.isLoadingDetails = false;
              this.errorMessage = getApiErrorMessage(error);
            });
      }
      else {
        resolve(changeControl);
      }
    });
    return promise;
  }


  isTheSameUser = (user) => this.authService.isTheSameUser(user);

  openInfoDialog(changeControl: ChangeControl) {
    this.addCertificateBeforeToChangeControl(changeControl)
      .then(changeCtrl => this.addValidationToChangeControl(changeCtrl)
        .then(validatedChangeControl => this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
          title: `Change Record: Pending`,
          tabs: getTrustedCertificatePendingChangesTabs(validatedChangeControl),
          displayName: getTrustedCertificateDisplayName,
          actionData: {
            errorMessage: {
              message: get(validatedChangeControl, 'errors') && ERROR_MESSAGES['trustedCertificateErrors'],
              warnings: get(validatedChangeControl, 'warnings'),
              errors: get(validatedChangeControl, 'errors')
            },
          }
        }))));
  }

  openTrustedCertificateDetailsDialog(trustedCertificate: TrustedCertificate) {
    this.addValidationToCertificate(trustedCertificate)
      .then(validatedCertificate => this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Trusted Certificate: ${validatedCertificate.certificateName}`,
        tabs: getTrustedCertificateDetailsTabs(validatedCertificate),
        displayName: getTrustedCertificateDisplayName,
        actionData: {
          errorMessage: {
            message: get(validatedCertificate, 'errors') && ERROR_MESSAGES['trustedCertificateErrors'],
            warnings: get(validatedCertificate, 'warnings'),
            errors: get(validatedCertificate, 'errors')
          },
        }
      })));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    this.addCertificateBeforeToChangeControl(changeControl)
      .then(changeCtrl => this.addValidationToChangeControl(changeCtrl)
        .then(validatedChangeControl =>
          this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
            title: 'Approve Change',
            tabs: getTrustedCertificatePendingChangesTabs(validatedChangeControl, true),
            yesCaption: 'Cancel',
            displayName: getTrustedCertificateDisplayName,
            actionData: {
              changeID: validatedChangeControl.changeID,
              changer: validatedChangeControl.changer,
              errorMessage: {
                message: (get(validatedChangeControl, 'errors') && ERROR_MESSAGES['trustedCertificateErrors'])
                  || (this.authService.isTheSameUser(validatedChangeControl.changer) ? ERROR_MESSAGES['approvingChanges'] : undefined),
                warnings: get(validatedChangeControl, 'warnings'),
                errors: get(validatedChangeControl, 'errors')
              },
              approveAction:
                (params: { changeID: string, status: string, approverComments: string }) =>
                  this.trustedCertificateService.resolveChange(params)
            }
          })).afterClosed().subscribe(data => {
            if (get(data, 'refreshList')) {
              this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
                title: `Trusted Certificate ${get(data, 'status').toLowerCase()}`,
                text:
                  `Trusted Certificate ${changeCtrl.trustedCertificateLog.certificateName} has been ${get(data, 'status').toLowerCase()}`,
                shouldHideYesCaption: true,
                noCaption: 'Back'
              })).afterClosed().subscribe(() => {
                this.getPendingChanges(this.pageIndex, this.pageSize);
              });
            }
          })));
  }

  deletePendingChange(changeControl: ChangeControl) {
    this.addCertificateBeforeToChangeControl(changeControl)
      .then(changeCtrl => this.addValidationToChangeControl(changeCtrl)
        .then((validatedChangeControl: ChangeControl) => this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
          title: `Delete ${validatedChangeControl.changeID}`,
          yesCaption: 'Cancel',
          tabs: getTrustedCertificatePendingChangesTabs(validatedChangeControl),
          displayName: getTrustedCertificateDisplayName,
          actionData: {
            errorMessage: {
              message: get(validatedChangeControl, 'errors') && ERROR_MESSAGES['trustedCertificateErrors'],
              warnings: get(validatedChangeControl, 'warnings'),
              errors: get(validatedChangeControl, 'errors')
            },
            shouldHideComments: true,
            id: validatedChangeControl.changeID,
            deleteAction: (id: string) => this.trustedCertificateService.deletePendingChange(id)
          },
        })).afterClosed().subscribe(data => {
          if (get(data, 'refreshList')) {
            this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
              title: `Pending Change deleted`,
              text: `The Pending change ${changeCtrl.changeID} has been deleted.`,
              shouldHideYesCaption: true,
              noCaption: 'Back'
            })).afterClosed().subscribe(() => {
              this.getPendingChanges(this.pageIndex, this.pageSize);
            });
          }
        })));
  }

  getCurrentRoute = () => this.router.url;

}
