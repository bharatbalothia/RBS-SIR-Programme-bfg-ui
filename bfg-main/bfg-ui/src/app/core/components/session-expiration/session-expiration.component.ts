import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { SessionTimerService } from '../../auth/session-timer.service';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { PasswordConfirmationDialogComponent } from 'src/app/shared/components/password-confirmation-dialog/password-confirmation-dialog.component';
import { AuthService } from '../../auth/auth.service';
import { NotificationService } from 'src/app/shared/services/notification.service';

@Component({
  selector: 'app-session-expiration',
  template: ''
})
export class SessionExpirationComponent implements OnInit, OnDestroy {

  @Input() startTimer?= true;

  private sessionTimerSubscription: Subscription;

  constructor(
    private sessionTimer: SessionTimerService,
    private passwordConfirmationDialog: MatDialog,
    private authService: AuthService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
    if (!this.sessionTimerSubscription && this.startTimer) {
      this.startSessionTime();
    }
  }

  startSessionTime() {
    const alertAtSec = this.authService.getAlertsAtMins() * 60;
    if (alertAtSec) {
      this.sessionTimer.startTimer();
      this.sessionTimerSubscription = this.sessionTimer.remainSeconds$.subscribe(t => {
        if (t === alertAtSec) {
          this.openPasswordConfirmationDialog();
        }
        if (t === 0) {
          this.stopSessionTime();
          this.passwordConfirmationDialog.closeAll();
          this.authService.logOut();
        }
      });
    }
  }

  restartSessionTime() {
    this.stopSessionTime();
    this.startSessionTime();
  }

  stopSessionTime() {
    this.sessionTimer.stopTimer();
    if (this.sessionTimerSubscription) {
      this.sessionTimerSubscription.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.passwordConfirmationDialog.closeAll();
    this.stopSessionTime();
  }

  openPasswordConfirmationDialog() {
    this.passwordConfirmationDialog.open(PasswordConfirmationDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Your session will expire in 10 minutes',
        subTitle: 'Please enter your password to extend it'
      }
    }).afterClosed().subscribe(password => {
      if (password) {
        this.authService.reLogIn({
          login: this.authService.getUserName(),
          password
        }).subscribe(
          () => {
            this.notificationService.show('Session', `Your session has been extended for ${this.authService.getTokenTimeLife()} minutes`, 'success');
            this.restartSessionTime();
          },
          (error) => this.openPasswordConfirmationDialog()
        );
      }
    });
  }
}
