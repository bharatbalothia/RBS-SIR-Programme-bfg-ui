import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { FileMonitorComponent } from './file-monitor/file-monitor.component';

export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: FileMonitorComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FileMonitorRoutingModule { }