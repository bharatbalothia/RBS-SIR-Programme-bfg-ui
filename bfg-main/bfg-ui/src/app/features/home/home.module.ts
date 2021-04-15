import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { HomeStatisticsComponent } from './home-statistics/home-statistics.component';
import { HomeEventsComponent } from './home-events/home-events.component';



@NgModule({
  declarations: [
    HomeComponent,
    HomeStatisticsComponent,
    HomeEventsComponent,
  ],
  imports: [
    SharedModule,
    HomeRoutingModule
  ]
})
export class HomeModule { }
