import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { routingPaths } from 'src/app/core/constants/routing-paths';
import { EntityHomeComponent } from './entity-home/entity-home.component';


export const routes: Routes = [
    { path: routingPaths.EMPTY, component: EntityHomeComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EntitiesRoutingModule { }
