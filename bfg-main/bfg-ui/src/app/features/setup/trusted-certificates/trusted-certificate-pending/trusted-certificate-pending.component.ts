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

@Component({
  selector: 'app-trusted-certificate-pending',
  templateUrl: './trusted-certificate-pending.component.html',
  styleUrls: ['./trusted-certificate-pending.component.scss']
})
export class TrustedCertificatePendingComponent implements OnInit {

  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;

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
    private dialog: MatDialog
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

  addValidationToChangeControl(changeControl: ChangeControl): Promise<any> {
    const certificateId = get(changeControl.trustedCertificateLog, 'certificateId');
    if (certificateId) {
      this.errorMessage = null;
      this.isLoadingDetails = true;
      return this.trustedCertificateService.validateCertificateById(certificateId.toString()).toPromise()
        .then(data => {
          this.isLoadingDetails = false;
          return { ...changeControl, certificateBefore: data, warnings: data.certificateWarnings };
        }, error => {
          this.isLoadingDetails = false;
          this.errorMessage = getApiErrorMessage(error);
          return false;
        });
    }
    else {
      return new Promise((res) => res(changeControl));
    }
  }

  openInfoDialog(changeControl: ChangeControl) {
    this.addValidationToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        tabs: getTrustedCertificatePendingChangesTabs(changeCtrl),
        displayName: getTrustedCertificateDisplayName
      })));
  }

  openTrustedCertificateDetailsDialog(trustedCertificate: TrustedCertificate) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `Trusted Certificate: ${trustedCertificate.certificateName}`,
      tabs: getTrustedCertificateDetailsTabs(trustedCertificate),
      displayName: getTrustedCertificateDisplayName
    }));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    this.addValidationToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        tabs: getTrustedCertificatePendingChangesTabs(changeCtrl),
        displayName: getTrustedCertificateDisplayName,
        actionData: {
          changeID: changeCtrl.changeID,
          changer: changeCtrl.changer,
          warnings: get(changeCtrl, 'warnings', null),
          approveAction:
            (params: { changeID: string, status: string, approverComments: string }) => this.trustedCertificateService.resolveChange(params)
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
      }));
  }
}
