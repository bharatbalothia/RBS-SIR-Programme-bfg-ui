import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import * as moment from 'moment';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class SessionTimerService {

  private count: number;
  private timer;
  private remainSeconds = new Subject<number>();
  public remainSeconds$ = this.remainSeconds.asObservable();

  startTimer() {
    this.stopTimer();
    const userData = sessionStorage.getItem('userData') && JSON.parse(sessionStorage.getItem('userData'));
    const accessToken = userData.accessToken || null;
    const expirationDate = new JwtHelperService().getTokenExpirationDate(accessToken);
    this.count = Math.round(moment.duration(moment(expirationDate).diff(moment())).asSeconds());

    this.timer = setInterval(() => {
      if (this.count > 0) {
        this.count--;
        this.remainSeconds.next(this.count);
      }}, 1000);
  }

  stopTimer() {
    clearInterval(this.timer);
  }

  resetTimer() {
    this.startTimer();
  }
}
