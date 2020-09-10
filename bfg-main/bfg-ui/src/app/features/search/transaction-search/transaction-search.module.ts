import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TransactionSearchComponent } from './transaction-search/transaction-search.component';
import { TransactionSearchRoutingModule } from './transaction-search-routing.module';

@NgModule({
    declarations: [
        TransactionSearchComponent
    ],
    imports: [
        SharedModule,
        TransactionSearchRoutingModule
    ]
})
export class TransactionSearchModule { }