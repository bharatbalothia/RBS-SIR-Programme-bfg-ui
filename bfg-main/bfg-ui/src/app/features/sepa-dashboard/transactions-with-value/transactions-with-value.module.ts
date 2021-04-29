import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TransactionsWithValueRoutingModule } from './transactions-with-value-routing.module';
import { TransactionsWithValueComponent } from './transactions-with-value/transactions-with-value.component';

@NgModule({
  declarations: [
  TransactionsWithValueComponent],
  imports: [
    SharedModule,
    TransactionsWithValueRoutingModule
  ]
})
export class TransactionsWithValueModule { }
