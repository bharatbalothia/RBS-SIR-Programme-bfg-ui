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
import { FileTableComponent } from './components/file-table/file-table.component';
import { TransactionTableComponent } from './components/transaction-table/transaction-table.component';
import { OverlayInfoComponent } from './components/overlay-info/overlay-info.component';
import { OverlayModule } from '@angular/cdk/overlay';
import { NotificationHttpInterceptor } from './services/notification-http.interceptor';
import { NotificationService } from './services/notification.service';
import { AutoRefreshService } from './services/autorefresh.service';
import { ToastrModule } from 'ngx-toastr';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { NgxMatDateFormats, NgxMatDatetimePickerModule, NGX_MAT_DATE_FORMATS } from '@angular-material-components/datetime-picker';
import { NgxMatMomentModule } from '@angular-material-components/moment-adapter';
import { InputUppercaseDirective } from './directives/input-uppercase/input-uppercase.directive';
import { PasswordConfirmationDialogComponent } from './components/password-confirmation-dialog/password-confirmation-dialog.component';
import { AutocompleteEnforceSelectionDirective } from './directives/autocomplete-enforce-selection/autocomplete-enforce-selection.directive';
import { AngularDraggableModule } from 'angular2-draggable';
import { AutorefreshDataComponent } from './components/autorefresh-data/autorefresh-data.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { MatBadgeModule } from '@angular/material/badge';

const DATE_FORMAT: NgxMatDateFormats = {
  parse: {
    dateInput: 'DD/MM/YYYY, HH:mm',
  },
  display: {
    dateInput: 'DD/MM/YYYY, HH:mm',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [
    // Components
    ConfirmDialogComponent,
    PasswordConfirmationDialogComponent,
    DetailsDialogComponent,
    DisplayTableCellComponent,
    TabContentComponent,
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
    AutorefreshDataComponent,

    // Directives
    InputLowercaseDirective,
    InputUppercaseDirective,
    NumberOnlyDirective,
    XmlPipe,
    DisableControlDirective,
    AutocompleteEnforceSelectionDirective,
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
    OverlayModule,
    ToastrModule.forRoot(),
    NgxMatDatetimePickerModule,
    NgxMatMomentModule,
    MatDatepickerModule,
    AngularDraggableModule,
    InfiniteScrollModule,
    MatBadgeModule
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
    OverlayModule,
    NgxMatDatetimePickerModule,
    NgxMatMomentModule,
    MatDatepickerModule,
    AngularDraggableModule,
    InfiniteScrollModule,
    MatBadgeModule,

    // Components
    ConfirmDialogComponent,
    PasswordConfirmationDialogComponent,
    DisplayTableCellComponent,
    TabContentComponent,
    ApprovingDialogComponent,
    TablePaginatorComponent,
    DeleteDialogComponent,
    TooltipComponent,
    DragableDialogWrapperComponent,
    TransmitDialogComponent,
    FileTableComponent,
    TransactionTableComponent,
    OverlayInfoComponent,
    AutorefreshDataComponent,

    // Directives
    InputLowercaseDirective,
    InputUppercaseDirective,
    NumberOnlyDirective,
    TooltipComponent,
    DisableControlDirective,
    AutocompleteEnforceSelectionDirective
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationHttpInterceptor,
      multi: true
    },
    NotificationService,
    AutoRefreshService,
    {
      provide: NGX_MAT_DATE_FORMATS,
      useValue: DATE_FORMAT
    }
    // { provide: OWL_DATE_TIME_LOCALE, useValue: 'en-GB' },
  ]
})
export class SharedModule { }
