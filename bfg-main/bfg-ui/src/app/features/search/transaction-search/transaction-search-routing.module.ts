import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { TransactionSearchComponent } from './transaction-search/transaction-search.component';

export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: TransactionSearchComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TransactionSearchRoutingModule { }