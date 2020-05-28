import { Component, OnInit } from '@angular/core';
import { Credentials } from '../../auth/credentials.model';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { getApiErrorMessage, ErrorMessage } from '../../utils/error-template';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoading = false;
  errorMessage: ErrorMessage;
  credentials: Credentials = {
    login: '',
    password: ''
  };

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

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

}
