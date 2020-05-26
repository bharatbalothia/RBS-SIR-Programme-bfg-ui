import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntitiesRoutingModule } from './entities-routing.module';
import { EntityCreateComponent } from './entity-create/entity-create.component';
import { EntityPendingComponent } from './entity-pending/entity-pending.component';



@NgModule({
  declarations: [
    EntityHomeComponent,
    EntityCreateComponent,
    EntityPendingComponent
  ],
  imports: [
    SharedModule,
    EntitiesRoutingModule
  ]
})
export class EntitiesModule { }
