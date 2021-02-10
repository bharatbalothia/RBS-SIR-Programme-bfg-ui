import { Component, OnInit } from '@angular/core';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { getTrustedCertificateDetailsTabs, getTrustedCertificateDisplayHeader, getTrustedCertificateDisplayName, getTrustedCertificatePendingChangesTabs } from '../trusted-certificate-display-names';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { MatTableDataSource } from '@angular/material/table';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { MatDialog } from '@angular/material/dialog';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { ChangeControlsWithPagination } from 'src/app/shared/models/changeControl/change-controls-with-pagination.model';
import { take } from 'rxjs/operators';
import { get } from 'lodash';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { Router } from '@angular/router';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { removeEmpties } from 'src/app/shared/utils/utils';

@Component({
  selector: 'app-trusted-certificate-pending',
  templateUrl: './trusted-certificate-pending.component.html',
  styleUrls: ['./trusted-certificate-pending.component.scss']
})
export class TrustedCertificatePendingComponent implements OnInit {

  ROUTING_PATHS = ROUTING_PATHS;
  CHANGE_OPERATION = CHANGE_OPERATION;

  certificateNameSearchingValue = '';
  thumbprintSearchingValue = '';
  thumbprint256SearchingValue = '';

  isLoading = true;
  isLoadingDetails = false;

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
    private notificationService: NotificationService,
    private toolTip: TooltipService
  ) { }

  ngOnInit() {
    this.getPendingChanges(this.pageIndex, this.pageSize);

    this.certificateNameSearchingValue = window.history.state.certificateNameSearchingValue || '';
    this.thumbprintSearchingValue = window.history.state.thumbprintSearchingValue || '';
    this.thumbprint256SearchingValue = window.history.state.thumbprint256SearchingValue || '';
  }

  getPendingChanges(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.trustedCertificateService.getPendingChanges(removeEmpties({
      certName: this.certificateNameSearchingValue || null,
      thumbprint: this.thumbprintSearchingValue || null,
      thumbprint256: this.thumbprint256SearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    }))
      .pipe(take(1)).subscribe((data: ChangeControlsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.changeControls = data;
        this.updateTable();
      },
        error => {
          this.isLoading = false;
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
          displayName: getTrustedCertificateDisplayHeader,
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
        displayName: getTrustedCertificateDisplayHeader,
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
            displayName: getTrustedCertificateDisplayHeader,
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
              this.notificationService.show(
                `Trusted Certificate ${get(data, 'status').toLowerCase()}`,
                `Trusted Certificate ${changeCtrl.trustedCertificateLog.certificateName} has been ${get(data, 'status').toLowerCase()}`,
                'success'
              );
              this.getPendingChanges(this.pageIndex, this.pageSize);
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
          displayName: getTrustedCertificateDisplayHeader,
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
            this.notificationService.show(
              `Pending Change deleted`,
              `The Pending change ${changeCtrl.changeID} has been deleted.`,
              'success'
            );
            this.getPendingChanges(this.pageIndex, this.pageSize);
          }
        })));
  }

  getCurrentRoute = () => this.router.url;

  getTooltip(step: string, field: string, mode?: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'trusted-cert',
      qualifier: step,
      mode: mode ? mode : 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : getTrustedCertificateDisplayName(field);
  }

  clearParams = () => {
    if (this.certificateNameSearchingValue !== '' || this.thumbprintSearchingValue !== '' || this.thumbprint256SearchingValue !== '') {
      this.certificateNameSearchingValue = '';
      this.thumbprintSearchingValue = '';
      this.thumbprint256SearchingValue = '';
      this.getPendingChanges(this.pageIndex, this.pageSize);
    }
  }
}
