import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { FileSearchComponent } from './file-search/file-search.component';

export const routes: Routes = [
    { path: ROUTING_PATHS.EMPTY, component: FileSearchComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FileSearchRoutingModule { }