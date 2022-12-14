import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { HomeSCTTrafficComponent } from './home-sct-traffic/home-sct-traffic.component';
import { HomeEventsComponent } from './home-events/home-events.component';
import { HomeAlertsComponent } from './home-alerts/home-alerts.component';
import { HomeEventComponent } from './home-events/home-event/home-event.component';



@NgModule({
  declarations: [
    HomeComponent,
    HomeSCTTrafficComponent,
    HomeEventsComponent,
    HomeAlertsComponent,
    HomeEventComponent,
  ],
  imports: [
    SharedModule,
    HomeRoutingModule
  ]
})
export class HomeModule { }
