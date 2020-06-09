import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutModule } from '@angular/cdk/layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

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

@NgModule({
  declarations: [
    // Components
    ConfirmDialogComponent,
    DetailsDialogComponent,
    // Directives
    InputLowercaseDirective,
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
  ],
  exports: [
    // Modules
    CommonModule,
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
    MatDialogModule,
    MatTableModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,

    // Components
    ConfirmDialogComponent,
    // Directives
    InputLowercaseDirective,
  ]
})
export class SharedModule { }