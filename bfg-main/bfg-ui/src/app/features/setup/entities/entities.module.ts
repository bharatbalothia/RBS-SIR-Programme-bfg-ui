import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntitiesRoutingModule } from './entities-routing.module';
import { EntityCreateComponent } from './entity-create/entity-create.component';
import { EntityPendingComponent } from './entity-pending/entity-pending.component';
import { EntitySearchComponent } from './entity-search/entity-search.component';
import { EntityScheduleDialogComponent } from './entity-schedule-dialog/entity-schedule-dialog.component';
import { EntityDeleteDialogComponent } from './entity-delete-dialog/entity-delete-dialog.component';



@NgModule({
  declarations: [
    EntityHomeComponent,
    EntityCreateComponent,
    EntityPendingComponent,
    EntitySearchComponent,
    EntityScheduleDialogComponent,
    EntityDeleteDialogComponent
  ],
  imports: [
    SharedModule,
    EntitiesRoutingModule
  ]
})
export class EntitiesModule { }
