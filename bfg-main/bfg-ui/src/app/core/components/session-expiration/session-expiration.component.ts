import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PasswordConfirmationDialogComponent } from 'src/app/shared/components/password-confirmation-dialog/password-confirmation-dialog.component';
import { AuthService } from '../../auth/auth.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../../auth/user.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-session-expiration',
  template: ''
})
export class SessionExpirationComponent implements OnInit, OnDestroy {

  private reloginTimeout;
  private logoutTimeout;
  private userSubscription: Subscription;

  constructor(
    private passwordConfirmationDialog: MatDialog,
    private authService: AuthService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
    this.userSubscription = this.authService.user.subscribe(user => {
      if (user) {
        this.startSessionTime(user);
      } else {
        this.clearTimeouts();
      }
    });
  }

  startSessionTime(user: User) {
    const alertAtSec = this.authService.getAlertsAtMins() * 60;
    const accessToken = user.accessToken || null;
    const expirationDate = new JwtHelperService().getTokenExpirationDate(accessToken);

    const timeout = expirationDate.getTime() - Date.now() - (alertAtSec * 1000);

    console.log('--- STARTED ---');

    console.log('EXP DATE: ', expirationDate);

    console.log('POPUP DATE: ', Math.round(timeout / 1000), Math.round(timeout / 1000 / 60), new Date(Date.now() + timeout));

    this.clearTimeouts();

    this.reloginTimeout = setTimeout(
      () => {
        console.log('LEFT (sec): ', (expirationDate.getTime() - Date.now()) / 1000);
        console.log('EXP DATE: ', expirationDate);
        this.openPasswordConfirmationDialog();
      }, timeout
    );

    this.logoutTimeout = setTimeout(
      () => {
        console.log('LOGOUT: ');
        this.stopSessionTime();
      }, expirationDate.getTime() - Date.now()
    );
  }

  stopSessionTime() {
    console.log('stop session time');
    this.clearTimeouts();
    this.passwordConfirmationDialog.closeAll();
    this.authService.logOut();
  }

  ngOnDestroy() {
    console.log('destroy');
    this.clearTimeouts();
    this.passwordConfirmationDialog.closeAll();
    this.userSubscription.unsubscribe();
  }

  clearTimeouts() {
    clearTimeout(this.reloginTimeout);
    clearTimeout(this.logoutTimeout);
  }

  openPasswordConfirmationDialog() {
    this.passwordConfirmationDialog.open(PasswordConfirmationDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: `Your session will expire in ${this.authService.getAlertsAtMins()} minutes`,
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
          },
          (error) => this.openPasswordConfirmationDialog()
        );
      }
    });
  }
}
