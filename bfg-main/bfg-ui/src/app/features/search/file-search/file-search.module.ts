import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FileSearchRoutingModule } from './file-search-routing.module';
import { FileSearchComponent } from './file-search/file-search.component';



@NgModule({
  declarations: [
    FileSearchComponent
  ],
  imports: [
    SharedModule,
    FileSearchRoutingModule
  ]
})
export class FileSearchModule { }
