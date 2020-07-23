import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TrustedCertificateHomeComponent } from './trusted-certificate-home/trusted-certificate-home.component';
import { TrustedCertificatesRoutingModule } from './trusted-certificates-routing.module';
import { TrustedCertificateCreateComponent } from './trusted-certificate-create/trusted-certificate-create.component';
import { TrustedCertificateSearchComponent } from './trusted-certificate-search/trusted-certificate-search.component';
import { TrustedCertificatePendingComponent } from './trusted-certificate-pending/trusted-certificate-pending.component';
import { TrustedCertificateApprovingDialogComponent } from './trusted-certificate-approving-dialog/trusted-certificate-approving-dialog.component';



@NgModule({
  declarations: [
    TrustedCertificateHomeComponent,
    TrustedCertificateCreateComponent,
    TrustedCertificateSearchComponent,
    TrustedCertificatePendingComponent,
    TrustedCertificateApprovingDialogComponent
  ],
  imports: [
    SharedModule,
    TrustedCertificatesRoutingModule
  ]
})
export class TrustedCertificatesModule { }
