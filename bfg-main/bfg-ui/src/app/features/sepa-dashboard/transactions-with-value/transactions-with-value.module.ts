import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TransactionsWithValueRoutingModule } from './transactions-with-value-routing.module';
import { TransactionsWithValueComponent } from './transactions-with-value/transactions-with-value.component';
import { TransactionsWithValueReportsDialogComponent } from './transactions-with-value/transactions-with-value-reports-dialog/transactions-with-value-reports-dialog.component';

@NgModule({
  declarations: [
  TransactionsWithValueComponent,
  TransactionsWithValueReportsDialogComponent],
  imports: [
    SharedModule,
    TransactionsWithValueRoutingModule
  ]
})
export class TransactionsWithValueModule { }
