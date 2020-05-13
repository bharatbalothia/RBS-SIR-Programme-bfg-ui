import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntitiesRoutingModule } from './entities-routing.module';
import { EntityCreateComponent } from './entity-create/entity-create.component';



@NgModule({
  declarations: [
    EntityHomeComponent,
    EntityCreateComponent
  ],
  imports: [
    SharedModule,
    EntitiesRoutingModule
  ]
})
export class EntitiesModule { }
