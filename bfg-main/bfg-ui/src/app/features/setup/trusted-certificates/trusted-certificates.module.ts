import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TrustedCertificateHomeComponent } from './trusted-certificate-home/trusted-certificate-home.component';
import { TrustedCertificatesRoutingModule } from './trusted-certificates-routing.module';
import { TrustedCertificateCreateComponent } from './trusted-certificate-create/trusted-certificate-create.component';



@NgModule({
  declarations: [
    TrustedCertificateHomeComponent,
    TrustedCertificateCreateComponent
  ],
  imports: [
    SharedModule,
    TrustedCertificatesRoutingModule
  ]
})
export class TrustedCertificatesModule { }
