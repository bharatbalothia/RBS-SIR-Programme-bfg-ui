import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';
import { HomeOverviewComponent } from './home-overview/home-overview.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, children: [
    { path: '', component: HomeOverviewComponent }
  ] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
