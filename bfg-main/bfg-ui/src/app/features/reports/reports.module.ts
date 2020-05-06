import { NgModule } from '@angular/core';
import { ReportsComponent } from './reports.component';
import { ReportsOverviewComponent } from './reports-overview/reports-overview.component';
import { ReportsRoutingModule } from './reports-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';



@NgModule({
  declarations: [ReportsComponent, ReportsOverviewComponent],
  imports: [
    SharedModule,
    ReportsRoutingModule
  ]
})
export class ReportsModule { }
