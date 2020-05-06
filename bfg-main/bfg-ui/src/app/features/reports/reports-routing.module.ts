import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReportsComponent } from './reports.component';
import { ReportsOverviewComponent } from './reports-overview/reports-overview.component';


export const routes: Routes = [
  { path: '', component: ReportsComponent, children: [
    { path: '', component: ReportsOverviewComponent }
  ] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
