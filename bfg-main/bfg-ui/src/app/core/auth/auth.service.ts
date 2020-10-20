import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { Credentials } from './credentials.model';
import { User } from './user.model';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { get } from 'lodash';
import { Params, Router } from '@angular/router';
import { ROUTING_PATHS } from '../constants/routing-paths';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { SSOCredentials } from './sso-credentials.model';


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
    private dialog: MatDialog
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

  ssoLogIn(credentials: SSOCredentials): Observable<User>{
    return this.http.post<User>(this.apiUrl + 'sso', credentials)
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

  isTheSameUser(user: string){
    return this.getUserName() === user;
  }

  getUserPermissions(): string[] {
    if (this.isAuthenticated()) {
      return this.jwtHelper.decodeToken(get(this.user.value, 'accessToken')).permissions;
    }
    return null;
  }

  isEnoughPermissions(requiredPermissions: string[]): boolean{
    let enoughPermissions = false;
    const userPermissions = this.getUserPermissions();
    if (!requiredPermissions || requiredPermissions.length === 0){
      return true;
    }
    requiredPermissions.forEach( element => enoughPermissions = enoughPermissions || userPermissions.includes(element));
    return enoughPermissions;
  }

  showForbidden(){
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Forbidden`,
      text: `You don't have enough permissions to proceed`,
      shouldHideYesCaption: true,
      noCaption: 'Back'
    }));
  }

  checkPermissions(requiredPermissions: string[]) {
    if (!this.isEnoughPermissions(requiredPermissions)){
      this.showForbidden();
    }
  }

  isValidSSOLink(queryParams): boolean{
    return queryParams && queryParams.userName && queryParams.nodeName && queryParams.dlssoToken;
  }

  extractSSOCredentials(queryParams: Params): SSOCredentials{
    const credentials: SSOCredentials = {userName : '', dlssoToken: '', nodeName: ''};
    if (this.isValidSSOLink(queryParams)){
      credentials.userName = queryParams.userName;
      credentials.dlssoToken = queryParams.dlssoToken;
      credentials.nodeName = queryParams.nodeName;
    }
    return credentials;
  }
}
