import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { ROUTING_PATHS } from './core/constants/routing-paths';
import { LayoutComponent } from './core/components/layout/layout.component';
import { LoginComponent } from './core/components/login/login.component';
import { AuthGuardService } from './core/auth/auth-guard.service';


const routes: Routes = [
  { path: ROUTING_PATHS.LOGIN, component: LoginComponent },
  {
    path: ROUTING_PATHS.EMPTY, component: LayoutComponent, canActivate: [AuthGuardService], children: [
      { path: ROUTING_PATHS.EMPTY, redirectTo: ROUTING_PATHS.HOME, pathMatch: 'full' },
      {
        path: ROUTING_PATHS.HOME,
        loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule)
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
      { path: ROUTING_PATHS.PAGE_NOT_FOUND, component: PageNotFoundComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
