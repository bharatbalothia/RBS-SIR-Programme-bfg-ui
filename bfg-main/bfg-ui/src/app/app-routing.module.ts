import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { routingPaths } from './core/constants/routing-paths';
import { LayoutComponent } from './core/components/layout/layout.component';


const routes: Routes = [
  {
    path: routingPaths.empty, component: LayoutComponent, children: [
      { path: routingPaths.empty, redirectTo: routingPaths.home, pathMatch: 'full' },
      { path: routingPaths.home, loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule) },
      { path: routingPaths.reports, loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule) },
    ]
  },
  { path: routingPaths.pageNotFound, component: PageNotFoundComponent }
];
[];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
