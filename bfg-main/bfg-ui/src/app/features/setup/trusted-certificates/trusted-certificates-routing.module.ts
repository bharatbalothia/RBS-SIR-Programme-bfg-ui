import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TrustedCertificatesHomeComponent } from './trusted-certificates-home/trusted-certificates-home.component';


export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: TrustedCertificatesHomeComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TrustedCertificatesRoutingModule { }
