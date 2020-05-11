import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { routingPaths } from './core/constants/routing-paths';
import { LayoutComponent } from './core/components/layout/layout.component';


const routes: Routes = [
  {
    path: routingPaths.EMPTY, component: LayoutComponent, children: [
      { path: routingPaths.EMPTY, redirectTo: routingPaths.HOME, pathMatch: 'full' },
      {
        path: routingPaths.HOME,
        loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule)
      },
      {
        path: routingPaths.ENTITIES,
        loadChildren: () => import('./features/setup/entities/entities.module').then(m => m.EntitiesModule)
      },
      {
        path: routingPaths.TRUSTED_CERTIFICATES,
        loadChildren: () => import('./features/setup/trusted-certificates/trusted-certificates.module')
          .then(m => m.TrustedCertificatesModule)
      },
      { path: routingPaths.PAGE_NOT_FOUND, component: PageNotFoundComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
