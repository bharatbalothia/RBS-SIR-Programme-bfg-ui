import { Component, OnInit, ViewChild } from '@angular/core';
import { Credentials } from '../../auth/credentials.model';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { getApiErrorMessage, ErrorMessage } from '../../utils/error-template';
import { FormGroup, FormControl, NgForm } from '@angular/forms';
import { LOGIN_FORM_VALIDATION_MESSAGES } from './login-form-validation-messages';
import { get } from 'lodash';
import { ROUTING_PATHS } from '../../constants/routing-paths';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  validationMessages = LOGIN_FORM_VALIDATION_MESSAGES;

  isLoading = false;
  errorMessage: ErrorMessage;
  credentials: Credentials = {
    login: '',
    password: ''
  };

  @ViewChild('form') loginForm: NgForm;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/' + ROUTING_PATHS.EMPTY]);
    }
  }

  isFormFieldHasError = (fieldKey: string, type: string) => get(this.loginForm, `controls.${fieldKey}.errors.${type}`, false);

  login() {
    this.errorMessage = null;
    this.isLoading = true;
    this.authService.logIn(this.credentials).subscribe(
      () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      (error) => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      }
    );
  }

  logout() {
    this.authService.logOut();
  }

  getUserName(){
    return this.authService.getUserName() || '';
  }

  isAuthenticated(){
    return this.authService.isAuthenticated();
  }

}
