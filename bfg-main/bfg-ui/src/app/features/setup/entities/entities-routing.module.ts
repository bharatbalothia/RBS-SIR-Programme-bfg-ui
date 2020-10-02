import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntityCreateComponent } from './entity-create/entity-create.component';
import { EntityPendingComponent } from './entity-pending/entity-pending.component';
import { EntitySearchComponent } from './entity-search/entity-search.component';
import { PermissionsGuardService } from 'src/app/core/guards/permissions-guard.service';


export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: EntityHomeComponent },
    {
        path: ROUTING_PATHS.CREATE, component: EntityCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                'SFG_UI_SCT_CREATE_ENTITY_SCT',
                'SFG_UI_SCT_CREATE_ENTITY_GPL'
            ]
        }
    },
    { path: ROUTING_PATHS.EDIT + '/:entityId', component: EntityCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                'SFG_UI_SCT_EDIT_ENTITY_SCT',
                'SFG_UI_SCT_EDIT_ENTITY_GPL'
            ]
        }
    },
    { path: ROUTING_PATHS.SEARCH, component: EntitySearchComponent },
    { path: ROUTING_PATHS.PENDING, component: EntityPendingComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EntitiesRoutingModule { }
