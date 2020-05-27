import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TrustedCertificatesHomeComponent } from './trusted-certificates-home/trusted-certificates-home.component';
import { TrustedCertificatesRoutingModule } from './trusted-certificates-routing.module';



@NgModule({
  declarations: [
    TrustedCertificatesHomeComponent
  ],
  imports: [
    SharedModule,
    TrustedCertificatesRoutingModule
  ]
})
export class TrustedCertificatesModule { }
