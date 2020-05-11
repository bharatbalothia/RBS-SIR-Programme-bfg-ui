import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntitiesRoutingModule } from './entities-routing.module';



@NgModule({
  declarations: [
    EntityHomeComponent
  ],
  imports: [
    SharedModule,
    EntitiesRoutingModule
  ]
})
export class EntitiesModule { }
