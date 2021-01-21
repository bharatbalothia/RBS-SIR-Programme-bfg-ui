import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
  selector: 'app-password-confirmation-dialog',
  templateUrl: './password-confirmation-dialog.component.html',
  styleUrls: ['./password-confirmation-dialog.component.scss']
})
export class PasswordConfirmationDialogComponent {
  password = '';

  constructor(
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
