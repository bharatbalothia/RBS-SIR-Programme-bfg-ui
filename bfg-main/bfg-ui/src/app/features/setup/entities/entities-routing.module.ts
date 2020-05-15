import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntityCreateComponent } from './entity-create/entity-create.component';
import { EntityPendingComponent } from './entity-pending/entity-pending.component';


export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: EntityHomeComponent },
    { path: ROUTING_PATHS.CREATE, component: EntityCreateComponent },
    { path: ROUTING_PATHS.PENDING, component: EntityPendingComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EntitiesRoutingModule { }
