import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FileSearchRoutingModule } from './file-search-routing.module';
import { FileSearchComponent } from './file-search/file-search.component';
import { TransactionsDialogComponent } from './transactions-dialog/transactions-dialog.component';
import { FileDetailsComponent } from './file-details/file-details.component';


@NgModule({
  declarations: [
    FileSearchComponent,
    TransactionsDialogComponent,
    FileDetailsComponent
  ],
  imports: [
    SharedModule,
    FileSearchRoutingModule
  ]
})
export class FileSearchModule { }
