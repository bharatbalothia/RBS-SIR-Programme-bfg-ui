import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { routingPaths } from './core/constants/routing-paths';
import { LayoutComponent } from './core/components/layout/layout.component';


const routes: Routes = [
  {
    path: routingPaths.EMPTY, component: LayoutComponent, children: [
      { path: routingPaths.EMPTY, redirectTo: routingPaths.HOME, pathMatch: 'full' },
      { path: routingPaths.HOME, loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule) },
      { path: routingPaths.REPORTS, loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule) },
    ]
  },
  { path: routingPaths.PAGE_NOT_FOUND, component: PageNotFoundComponent }
];
[];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
