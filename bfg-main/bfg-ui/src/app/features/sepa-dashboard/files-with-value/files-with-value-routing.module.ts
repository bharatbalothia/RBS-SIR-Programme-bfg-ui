import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { FilesWithValueComponent } from './files-with-value/files-with-value.component';


export const routes: Routes = [
  { path: ROUTING_PATHS.EMPTY, component: FilesWithValueComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FilesWithValueRoutingModule { }
