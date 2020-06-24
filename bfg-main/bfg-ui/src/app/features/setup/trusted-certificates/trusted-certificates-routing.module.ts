import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TrustedCertificateHomeComponent } from './trusted-certificate-home/trusted-certificate-home.component';
import { TrustedCertificateCreateComponent } from './trusted-certificate-create/trusted-certificate-create.component';


export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: TrustedCertificateHomeComponent },
    { path: ROUTING_PATHS.CREATE, component: TrustedCertificateCreateComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TrustedCertificatesRoutingModule { }
