import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/auth/auth.service';
import { PasswordConfirmationDialogData } from './password-confirmation-dialog-data.model';

@Component({
  selector: 'app-password-confirmation-dialog',
  templateUrl: './password-confirmation-dialog.component.html',
  styleUrls: ['./password-confirmation-dialog.component.scss']
})
export class PasswordConfirmationDialogComponent implements OnInit {
  password = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: PasswordConfirmationDialogData,
    private authService: AuthService,
    private dialogRef: MatDialogRef<PasswordConfirmationDialogComponent>
  ) {}

  ngOnInit() {
    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === 'Escape') {
        this.onCancel();
      } else if (event.key === 'Enter') {
        this.onSubmit();
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.password) {
      this.dialogRef.close(this.password);
    }
  }

  getUserName() {
    return this.authService.getUserName() || '';
  }
 }
