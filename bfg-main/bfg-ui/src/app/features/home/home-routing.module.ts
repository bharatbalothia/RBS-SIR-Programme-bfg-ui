import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';

export const routes: Routes = [
  { path: ROUTING_PATHS.EMPTY, component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
