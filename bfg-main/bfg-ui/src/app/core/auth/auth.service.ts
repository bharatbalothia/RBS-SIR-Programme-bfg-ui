import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { Credentials } from './credentials.model';
import { User } from './user.model';
import { tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private USER_STOARGE_NAME = 'userData';
  user = new BehaviorSubject<User>(null);

  constructor(private http: HttpClient) { }

  private apiUrl: string = environment.apiUrl + 'auth/';

  autoLogIn() {
    const userData: User = JSON.parse(localStorage.getItem(this.USER_STOARGE_NAME));
    if (userData) {
      this.user.next(userData);
    }
  }

  logIn(credentials: Credentials): Observable<User> {
    return this.http.post<User>(this.apiUrl + 'signin', credentials)
      .pipe(
        tap(user => {
          user._createdAt = Date.now();
          this.user.next(user);
          localStorage.setItem(this.USER_STOARGE_NAME, JSON.stringify(user));
        })
      );
  }

  logOut() {
    this.user.next(null);
  }
}
