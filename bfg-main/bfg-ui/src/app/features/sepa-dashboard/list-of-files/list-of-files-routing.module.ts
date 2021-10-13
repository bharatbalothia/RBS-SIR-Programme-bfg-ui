import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ListOfFilesComponent } from './list-of-files/list-of-files.component';

export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: ListOfFilesComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ListOfFilesRoutingModule { }
