import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/auth/auth.service';
import { PasswordConfirmationDialogData } from './password-confirmation-dialog-data.model';

@Component({
  selector: 'app-password-confirmation-dialog',
  templateUrl: './password-confirmation-dialog.component.html',
  styleUrls: ['./password-confirmation-dialog.component.scss']
})
export class PasswordConfirmationDialogComponent {
  password = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: PasswordConfirmationDialogData,
    private authService: AuthService,
    private dialog: MatDialogRef<PasswordConfirmationDialogComponent>
  ) {}

  onCancel(): void {
    this.dialog.close();
  }

  getUserName() {
    return this.authService.getUserName() || '';
  }
}
