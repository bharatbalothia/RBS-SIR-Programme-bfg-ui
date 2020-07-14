import { Component, OnInit } from '@angular/core';
import { getTrustedCertificateDisplayName, getTrustedCertificateDetailsTabs } from '../trusted-certificate-display-names';
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

@Component({
  selector: 'app-trusted-certificate-search',
  templateUrl: './trusted-certificate-search.component.html',
  styleUrls: ['./trusted-certificate-search.component.scss']
})
export class TrustedCertificateSearchComponent implements OnInit {

  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;

  entityNameSearchingValue = '';
  serviceSearchingValue = '';

  isLoading = true;
  trustedCertificates: TrustedCertificatesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'name', 'thumbprint'];
  dataSource: MatTableDataSource<TrustedCertificate>;

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20];

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
    this.trustedCertificateService.getTrustedCertificateList(removeEmpties({
      entity: this.entityNameSearchingValue || null,
      service: this.serviceSearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).subscribe((data: TrustedCertificatesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.trustedCertificates = data;
      this.updateTable();
    });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.trustedCertificates.content);
  }

  openTrustedCertificateDetailsDialog(trustedCertificate: TrustedCertificate) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `Trusted Certificate: ${trustedCertificate.name}`,
      tabs: getTrustedCertificateDetailsTabs(trustedCertificate),
    }));
  }

}
