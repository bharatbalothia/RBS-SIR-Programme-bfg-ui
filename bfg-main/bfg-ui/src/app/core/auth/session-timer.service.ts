import { Injectable } from '@angular/core';
import { interval, Observable, Subject, Subscription } from 'rxjs';
import * as moment from 'moment';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class SessionTimerService {

  private count: number;
  private timerSubscription: Subscription;
  private timer: Observable<number> = interval(1000);
  private remainSeconds = new Subject<number>();
  public remainSeconds$ = this.remainSeconds.asObservable();

  startTimer() {
    this.stopTimer();
    const userData = sessionStorage.getItem('userData') && JSON.parse(sessionStorage.getItem('userData'));
    const accessToken = userData.accessToken || null;
    const expirationDate = new JwtHelperService().getTokenExpirationDate(accessToken);
    this.count = Math.round(moment.duration(moment(expirationDate).diff(moment())).asSeconds());

    this.timerSubscription = this.timer.subscribe(n => {
      if (this.count > 0) {
        this.count--;
        this.remainSeconds.next(this.count);
      }
    });
  }

  stopTimer() {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  resetTimer() {
    this.startTimer();
  }
}
