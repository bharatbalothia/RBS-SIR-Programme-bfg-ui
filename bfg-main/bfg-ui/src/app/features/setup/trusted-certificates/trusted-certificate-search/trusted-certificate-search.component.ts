import { Component, OnInit } from '@angular/core';
import { getTrustedCertificateDisplayName, getTrustedCertificateDetailsTabs, getTrustedCertificatePendingChangesTabs } from '../trusted-certificate-display-names';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TrustedCertificatesWithPagination } from 'src/app/shared/models/trustedCertificate/trusted-certificates-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { MatDialog } from '@angular/material/dialog';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { get } from 'lodash';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';

@Component({
  selector: 'app-trusted-certificate-search',
  templateUrl: './trusted-certificate-search.component.html',
  styleUrls: ['./trusted-certificate-search.component.scss']
})
export class TrustedCertificateSearchComponent implements OnInit {

  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;

  certificateNameSearchingValue = '';
  thumbprintSearchingValue = '';
  thumbprint256SearchingValue = '';

  errorMessage: ErrorMessage;

  isLoading = true;
  isLoadingDetails = false;
  trustedCertificates: TrustedCertificatesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'name', 'thumbprint', 'thumbprint256'];
  dataSource: MatTableDataSource<TrustedCertificate>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private trustedCertificateService: TrustedCertificateService,
    private authService: AuthService,
    private dialog: MatDialog,
    private toolTip: TooltipService,
  ) { }

  ngOnInit(): void {
    if (window.history.state.pageIndex && window.history.state.pageSize) {
      this.pageIndex = window.history.state.pageIndex;
      this.pageSize = window.history.state.pageSize;
    }
    this.getTrustedCertificateList(this.pageIndex, this.pageSize);
  }

  getTrustedCertificateList(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.errorMessage = null;
    this.trustedCertificateService.getTrustedCertificateList(removeEmpties({
      certName: this.certificateNameSearchingValue || null,
      thumbprint: this.thumbprintSearchingValue || null,
      thumbprint256: this.thumbprint256SearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).subscribe((data: TrustedCertificatesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.trustedCertificates = data;
      this.updateTable();
    },
      (error) => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.trustedCertificates.content);
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
                this.getTrustedCertificateList(this.pageIndex, this.pageSize);
              });
            }
          })));
  }

  deleteTrustedCertificate(trustedCertificate: TrustedCertificate) {
    this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
      title: `Delete ${trustedCertificate.certificateName}`,
      yesCaption: 'Cancel',
      tabs: getTrustedCertificateDetailsTabs(trustedCertificate),
      displayName: getTrustedCertificateDisplayName,
      actionData: {
        id: trustedCertificate.certificateId,
        deleteAction: (id: string, changerComments: string) => this.trustedCertificateService.deleteTrustedCertificate(id, changerComments)
      },
      tooltip: this.getTooltip('delete-tc', 'changerComments', 'delete')
    })).afterClosed().subscribe(data => {
      if (get(data, 'refreshList')) {
        this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
          title: `Trusted Certificate deleted`,
          text: `The update to the Trusted Certificate will be committed after the change has been approved.`,
          shouldHideYesCaption: true,
          noCaption: 'Back'
        })).afterClosed().subscribe(() => {
          this.getTrustedCertificateList(this.pageIndex, this.pageSize);
        });
      }
    });
  }

  getTooltip(step: string, field: string, mode?: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'trusted-cert',
      qualifier: step,
      mode: mode ? mode : 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getTrustedCertificateDisplayName(field);
  }
}
