import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ListOfFilesRoutingModule } from './list-of-files-routing.module';
import { ListOfFilesComponent } from './list-of-files/list-of-files.component';

@NgModule({
    declarations: [
    ListOfFilesComponent],
    imports: [
        SharedModule,
        ListOfFilesRoutingModule
    ]
})
export class ListOfFilesModule { }
