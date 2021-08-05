import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { FilesWithValueRoutingModule } from './files-with-value-routing.module';
import { FilesWithValueComponent } from './files-with-value/files-with-value.component';
import { FilesWithValueReportsDialogComponent } from './files-with-value/files-with-value-reports-dialog/files-with-value-reports-dialog.component';

@NgModule({
  declarations: [
    FilesWithValueComponent,
    FilesWithValueReportsDialogComponent],
  imports: [
    SharedModule,
    FilesWithValueRoutingModule
  ]
})
export class TransactionsWithValueModule { }
