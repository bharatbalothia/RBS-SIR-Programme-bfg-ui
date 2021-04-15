import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { HomeStatisticsComponent } from './home-statistics/home-statistics.component';



@NgModule({
  declarations: [
    HomeComponent,
    HomeStatisticsComponent,
  ],
  imports: [
    SharedModule,
    HomeRoutingModule
  ]
})
export class HomeModule { }
