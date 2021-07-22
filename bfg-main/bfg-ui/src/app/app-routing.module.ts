import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { ROUTING_PATHS } from './core/constants/routing-paths';
import { LoginComponent } from './core/components/login/login.component';
import { AuthGuardService } from './core/auth/auth-guard.service';
import { PermissionsGuardService } from './core/guards/permissions-guard.service';
import { ChildLayoutComponent } from './core/components/child-layout/child-layout.component';
import { SEPADashboardGuardService } from './shared/models/sepa-dashboard/sepa-dashboard-guard.service';
import { GlobalPermissionGuardService } from './core/guards/global-permission-guard.service';


const routes: Routes = [
  { path: ROUTING_PATHS.LOGIN, component: LoginComponent },
  {
    path: ROUTING_PATHS.EMPTY,
    component: ChildLayoutComponent,
    canActivate: [AuthGuardService],
    children: [
      {
        path: ROUTING_PATHS.EMPTY,
        redirectTo: ROUTING_PATHS.HOME,
        pathMatch: 'full',
      },
      {
        path: ROUTING_PATHS.HOME,
        canActivate: [GlobalPermissionGuardService],
        loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule)
      },
      {
        path: ROUTING_PATHS.FILE_MONITOR,
        canActivate: [GlobalPermissionGuardService],
        loadChildren: () => import('./features/monitor/file-monitor/file-monitor.module').then(m => m.FileMonitorModule)
      },
      {
        path: ROUTING_PATHS.ERROR_MONITOR,
        canActivate: [GlobalPermissionGuardService],
        loadChildren: () => import('./features/monitor/error-monitor/error-monitor.module').then(m => m.ErrorMonitorModule)
      },
      {
        path: ROUTING_PATHS.FILE_SEARCH,
        canActivate: [GlobalPermissionGuardService],
        loadChildren: () => import('./features/search/file-search/file-search.module').then(m => m.FileSearchModule)
      },
      {
        path: ROUTING_PATHS.SCT_TRANSACTION_SEARCH,
        canActivate: [GlobalPermissionGuardService],
        loadChildren: () => import('./features/search/transaction-search/transaction-search.module').then(m => m.TransactionSearchModule)
      },
      {
        path: ROUTING_PATHS.ENTITIES,
        loadChildren: () => import('./features/setup/entities/entities.module').then(m => m.EntitiesModule)
      },
      {
        path: ROUTING_PATHS.TRUSTED_CERTIFICATES,
        loadChildren: () => import('./features/setup/trusted-certificates/trusted-certificates.module')
          .then(m => m.TrustedCertificatesModule)
      },
      {
        path: ROUTING_PATHS.TRANSACTIONS_WITH_VALUE,
        canActivate: [GlobalPermissionGuardService, SEPADashboardGuardService],
        loadChildren: () => import('./features/sepa-dashboard/transactions-with-value/transactions-with-value.module')
          .then(m => m.TransactionsWithValueModule)
      },
      {
        path: ROUTING_PATHS.LIST_OF_FILES,
        canActivate: [GlobalPermissionGuardService, SEPADashboardGuardService],
        loadChildren: () => import('./features/sepa-dashboard/list-of-files/list-of-files.module')
          .then(m => m.ListOfFilesModule)
      },
      { path: ROUTING_PATHS.PAGE_NOT_FOUND, component: PageNotFoundComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
