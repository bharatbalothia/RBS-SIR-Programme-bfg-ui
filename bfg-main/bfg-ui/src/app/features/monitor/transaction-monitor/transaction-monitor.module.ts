import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { TransactionMonitorComponent } from './transaction-monitor/transaction-monitor.component';
import { TransactionMonitorRoutingModule } from './transaction-monitor-routing.module';

@NgModule({
    declarations: [
        TransactionMonitorComponent,
    ],
    imports: [
        SharedModule,
        TransactionMonitorRoutingModule
    ]
})
export class TransactionMonitorModule { }
