import { Component, OnInit, ViewChild } from '@angular/core';
import { Credentials } from '../../auth/credentials.model';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { LOGIN_FORM_VALIDATION_MESSAGES } from './login-form-validation-messages';
import { get } from 'lodash';
import { ROUTING_PATHS } from '../../constants/routing-paths';
import { VersionService } from 'src/app/shared/services/version.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  validationMessages = LOGIN_FORM_VALIDATION_MESSAGES;

  isLoading = false;
  credentials: Credentials = {
    login: '',
    password: ''
  };

  @ViewChild('form') loginForm: NgForm;

  appVersion: string;

  constructor(
    private authService: AuthService,
    private router: Router,
    private versionService: VersionService
  ) { }

  ngOnInit(): void {
    this.versionService.applicationVersion.subscribe(data => this.appVersion = data);
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/' + ROUTING_PATHS.EMPTY]);
    }
  }

  isFormFieldHasError = (fieldKey: string, type: string) => get(this.loginForm, `controls.${fieldKey}.errors.${type}`, false);

  login() {
    this.isLoading = true;
    this.authService.logIn(this.credentials).subscribe(
      () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      (error) => {
        this.isLoading = false;
      }
    );
  }

  logout() {
    this.authService.logOut();
  }

  getUserName() {
    return this.authService.getUserName() || '';
  }

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }

}
