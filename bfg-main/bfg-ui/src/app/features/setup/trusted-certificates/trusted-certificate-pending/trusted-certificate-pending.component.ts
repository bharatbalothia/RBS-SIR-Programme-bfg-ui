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
import { TrustedCertificateApprovingDialogComponent } from '../trusted-certificate-approving-dialog/trusted-certificate-approving-dialog.component';

@Component({
  selector: 'app-trusted-certificate-pending',
  templateUrl: './trusted-certificate-pending.component.html',
  styleUrls: ['./trusted-certificate-pending.component.scss']
})
export class TrustedCertificatePendingComponent implements OnInit {

  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;

  isLoading = true;
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
      });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.changeControls.content);
  }

  addCertificateBeforeToChangeControl(changeControl: ChangeControl): Promise<ChangeControl> {
    const certificateId = get(changeControl.trustedCertificateLog, 'certificateId');
    if (certificateId) {
      return this.trustedCertificateService.getCertificateById(certificateId.toString()).toPromise()
        .then(data => ({ ...changeControl, certificateBefore: data }), () => changeControl);
    }
    else {
      return new Promise((res) => res(changeControl));
    }
  }

  openInfoDialog(changeControl: ChangeControl) {
    this.addCertificateBeforeToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(TrustedCertificateApprovingDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        tabs: getTrustedCertificatePendingChangesTabs(changeCtrl),
      })));
  }

  openTrustedCertificateDetailsDialog(trustedCertificate: TrustedCertificate) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `Trusted Certificate: ${trustedCertificate.certificateName}`,
      tabs: getTrustedCertificateDetailsTabs(trustedCertificate),
    }));
  }

  // openApprovingDialog(changeControl: ChangeControl) {
  //   this.addEntityBeforeToChangeControl(changeControl).then(changeCtrl =>
  //     this.dialog.open(EntityApprovingDialogComponent, new DetailsDialogConfig({
  //       title: 'Approve Change',
  //       tabs: getPendingChangesTabs(changeCtrl),
  //       actionData: {
  //         changeID: changeControl.changeID,
  //         changer: changeControl.changer,
  //         isApproveActions: true
  //       }
  //     })).afterClosed().subscribe(data => {
  //       if (get(data, 'refreshList')) {
  //         this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
  //           title: `Entity ${get(data, 'status').toLowerCase()}`,
  //           text: `Entity ${changeControl.entityLog.entity} has been ${get(data, 'status').toLowerCase()}`,
  //           shouldHideYesCaption: true,
  //           noCaption: 'Back'
  //         })).afterClosed().subscribe(() => {
  //           this.getPendingChanges(this.pageIndex, this.pageSize);
  //         });
  //       }
  //     }));
  // }
}
