import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TrustedCertificateHomeComponent } from './trusted-certificate-home/trusted-certificate-home.component';
import { TrustedCertificateCreateComponent } from './trusted-certificate-create/trusted-certificate-create.component';
import { PermissionsGuardService } from 'src/app/core/guards/permissions-guard.service';
import { TrustedCertificateSearchComponent } from './trusted-certificate-search/trusted-certificate-search.component';
import { TrustedCertificatePendingComponent } from './trusted-certificate-pending/trusted-certificate-pending.component';
import { TRUSTED_CERTIFICATE_PERMISSIONS } from 'src/app/shared/models/trustedCertificate/trusted-certificate-constants';


export const routes: Routes = [
    {
        path: ROUTING_PATHS.EMPTY, component: TrustedCertificateHomeComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.VIEW]
        }
    },
    {
        path: ROUTING_PATHS.CREATE, component: TrustedCertificateCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.CREATE]
        }
    },
    {
        path: ROUTING_PATHS.PENDING + '/' + ROUTING_PATHS.EDIT + '/:changeId', component: TrustedCertificateCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.CREATE]
        }
    },
    {
        path: ROUTING_PATHS.SEARCH, component: TrustedCertificateSearchComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.VIEW]
        }
    },
    {
        path: ROUTING_PATHS.PENDING, component: TrustedCertificatePendingComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.VIEW]
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TrustedCertificatesRoutingModule { }
