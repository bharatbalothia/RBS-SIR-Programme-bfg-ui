import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutModule } from '@angular/cdk/layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTreeModule } from '@angular/material/tree';
import { MatCardModule } from '@angular/material/card';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatRadioModule } from '@angular/material/radio';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DetailsDialogComponent } from './components/details-dialog/details-dialog.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { InputLowercaseDirective } from './directives/input-lowercase/input-lowercase.directive';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTabsModule } from '@angular/material/tabs';
import { DisplayTableCellComponent } from './components/display-table-cell/display-table-cell.component';
import { TabContentComponent } from './components/tab-content/tab-content.component';
import { ErrorTemplateComponent } from './components/error-template/error-template.component';
import { ApprovingDialogComponent } from './components/approving-dialog/approving-dialog.component';
import { NumberOnlyDirective } from './directives/number-only.directive';
import { TablePaginatorComponent } from './components/table-paginator/table-paginator.component';
import { DeleteDialogComponent } from './components/delete-dialog/delete-dialog.component';
import { TooltipComponent } from './components/tooltip/tooltip.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { DragableDialogWrapperComponent } from './components/dragable-dialog-wrapper/dragable-dialog-wrapper.component';
import { XmlPipe } from './pipes/xml/xml.pipe';
import { DisableControlDirective } from './directives/disable-control.directive';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { TransmitDialogComponent } from './components/transmit-dialog/transmit-dialog.component';
import { BusinessProcessDialogComponent } from './components/business-process-dialog/business-process-dialog.component';
import { NgxDaterangepickerMd } from 'ngx-daterangepicker-material';
import { FileTableComponent } from './components/file-table/file-table.component';
import { TransactionTableComponent } from './components/transaction-table/transaction-table.component';
import { OverlayInfoComponent } from './components/overlay-info/overlay-info.component';
import { OverlayModule } from '@angular/cdk/overlay';
import { NotificationHttpInterceptor } from './services/NotificationHttpInterceptor';
import { NotificationService } from './services/NotificationService';
import { ToastrModule } from 'ngx-toastr';


@NgModule({
  declarations: [
    // Components
    ConfirmDialogComponent,
    DetailsDialogComponent,
    DisplayTableCellComponent,
    TabContentComponent,
    ErrorTemplateComponent,
    ApprovingDialogComponent,
    TablePaginatorComponent,
    DeleteDialogComponent,
    TooltipComponent,
    DragableDialogWrapperComponent,
    TransmitDialogComponent,
    BusinessProcessDialogComponent,
    FileTableComponent,
    TransactionTableComponent,
    OverlayInfoComponent,

    // Directives
    InputLowercaseDirective,
    NumberOnlyDirective,
    XmlPipe,
    DisableControlDirective,
  ],
  imports: [
    CommonModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatTreeModule,
    MatCardModule,
    MatStepperModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatAutocompleteModule,
    MatRadioModule,
    HttpClientModule,
    MatDialogModule,
    MatTableModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatTabsModule,
    DragDropModule,
    MatSlideToggleModule,
    NgxDaterangepickerMd.forRoot({
      format: 'DD/MM/YYYY, HH:mm',
      displayFormat: 'DD/MM/YYYY, HH:mm',
    }),
    OverlayModule,
    ToastrModule.forRoot()
  ],
  exports: [
    // Modules
    CommonModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatTreeModule,
    MatCardModule,
    MatStepperModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatAutocompleteModule,
    MatRadioModule,
    HttpClientModule,
    MatDialogModule,
    MatTableModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatTabsModule,
    DragDropModule,
    MatSlideToggleModule,
    NgxDaterangepickerMd,
    OverlayModule,

    // Components
    ConfirmDialogComponent,
    DisplayTableCellComponent,
    TabContentComponent,
    ErrorTemplateComponent,
    ApprovingDialogComponent,
    TablePaginatorComponent,
    DeleteDialogComponent,
    TooltipComponent,
    DragableDialogWrapperComponent,
    TransmitDialogComponent,
    FileTableComponent,
    TransactionTableComponent,
    OverlayInfoComponent,

    // Directives
    InputLowercaseDirective,
    NumberOnlyDirective,
    TooltipComponent,
    DisableControlDirective
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationHttpInterceptor,
      multi: true
    },
    NotificationService,
    // { provide: OWL_DATE_TIME_LOCALE, useValue: 'en-GB' },
  ]
})
export class SharedModule { }
