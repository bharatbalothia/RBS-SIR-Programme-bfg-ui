import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TransactionsWithValueComponent } from './transactions-with-value/transactions-with-value.component';


export const routes: Routes = [
  { path: ROUTING_PATHS.EMPTY, component: TransactionsWithValueComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionsWithValueRoutingModule { }
