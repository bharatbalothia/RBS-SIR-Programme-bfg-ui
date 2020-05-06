import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { HomeOverviewComponent } from './home-overview/home-overview.component';
import { HomeRoutingModule } from './home-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';



@NgModule({
  declarations: [
    HomeComponent,
    HomeOverviewComponent,
  ],
  imports: [
    SharedModule,
    HomeRoutingModule
  ]
})
export class HomeModule { }
