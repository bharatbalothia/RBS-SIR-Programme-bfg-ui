import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { FileMonitorComponent } from './file-monitor/file-monitor.component';
import { FileMonitorRoutingModule } from './file-monitor-routing.module';

@NgModule({
    declarations: [
        FileMonitorComponent
    ],
    imports: [
        SharedModule,
        FileMonitorRoutingModule
    ]
})
export class FileMonitorModule { }
