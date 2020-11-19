import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { EntityHomeComponent } from './entity-home/entity-home.component';
import { EntityCreateComponent } from './entity-create/entity-create.component';
import { EntityPendingComponent } from './entity-pending/entity-pending.component';
import { EntitySearchComponent } from './entity-search/entity-search.component';
import { PermissionsGuardService } from 'src/app/core/guards/permissions-guard.service';
import { ENTITY_PERMISSIONS } from 'src/app/shared/models/entity/entity-constants';


export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: EntityHomeComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                ...Object.keys(ENTITY_PERMISSIONS).map(key => ENTITY_PERMISSIONS[key])
            ]
        }
    },
    {
        path: ROUTING_PATHS.CREATE, component: EntityCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                ENTITY_PERMISSIONS.CREATE_SCT,
                ENTITY_PERMISSIONS.CREATE_GPL
            ]
        }
    },
    { path: ROUTING_PATHS.EDIT + '/:entityId', component: EntityCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                ENTITY_PERMISSIONS.EDIT_SCT,
                ENTITY_PERMISSIONS.EDIT_GPL
            ]
        }
    },
    { path: ROUTING_PATHS.PENDING + '/' + ROUTING_PATHS.EDIT + '/:changeId', component: EntityCreateComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                // ENTITY_PERMISSIONS.EDIT_SCT,
                // ENTITY_PERMISSIONS.EDIT_GPL
            ]
        }
    },
    { path: ROUTING_PATHS.SEARCH, component: EntitySearchComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                ENTITY_PERMISSIONS.VIEW
            ]
        }
    },
    { path: ROUTING_PATHS.PENDING, component: EntityPendingComponent,
        canActivate: [PermissionsGuardService],
        data: {
            permissions: [
                ENTITY_PERMISSIONS.VIEW
            ]
        } }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EntitiesRoutingModule { }
