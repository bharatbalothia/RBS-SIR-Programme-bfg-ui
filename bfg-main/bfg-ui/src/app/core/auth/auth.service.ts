import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { Credentials } from './credentials.model';
import { User } from './user.model';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { get } from 'lodash';
import { Router } from '@angular/router';
import { ROUTING_PATHS } from '../constants/routing-paths';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private USER_STORAGE_NAME = 'userData';
  user = new BehaviorSubject<User>(null);

  public jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(
    private http: HttpClient,
    private router: Router,
  ) { }

  private apiUrl: string = environment.apiUrl + 'auth/';

  autoLogIn() {
    if (!this.user.value) {
      const userData: User = JSON.parse(localStorage.getItem(this.USER_STORAGE_NAME));
      if (userData) {
        this.user.next(userData);
      }
    }
  }

  logIn(credentials: Credentials): Observable<User> {
    return this.http.post<User>(this.apiUrl + 'signin', credentials)
      .pipe(
        tap(user => {
          this.user.next(user);
          localStorage.setItem(this.USER_STORAGE_NAME, JSON.stringify(user));
        })
      );
  }

  logOut() {
    this.user.next(null);
    localStorage.removeItem(this.USER_STORAGE_NAME);
    this.router.navigate(['/' + ROUTING_PATHS.LOGIN]);
  }

  public isAuthenticated(): boolean {
    this.autoLogIn();
    return !this.jwtHelper.isTokenExpired(get(this.user.value, 'accessToken'));
  }

  getUserName(): string {
    if (this.isAuthenticated()) {
      return this.jwtHelper.decodeToken(get(this.user.value, 'accessToken')).sub;
    }
    return null;
  }

}
