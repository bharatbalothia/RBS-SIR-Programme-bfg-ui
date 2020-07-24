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
    private dialog: MatDialog
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
            this.getTrustedCertificateList(this.pageIndex, this.pageSize);
          });
        }
      }));
  }

}
