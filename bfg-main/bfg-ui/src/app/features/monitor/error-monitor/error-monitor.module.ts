import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ErrorMonitorRoutingModule } from './error-monitor-routing.module';
import { ErrorMonitorComponent } from './error-monitor/error-monitor.component';

@NgModule({
    declarations: [
        ErrorMonitorComponent],
    imports: [
        SharedModule,
        ErrorMonitorRoutingModule
    ]
})
export class ErrorMonitorModule { }
