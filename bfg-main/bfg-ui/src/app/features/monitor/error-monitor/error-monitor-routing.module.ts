import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ErrorMonitorComponent } from './error-monitor/error-monitor.component';

export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: ErrorMonitorComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ErrorMonitorRoutingModule { }