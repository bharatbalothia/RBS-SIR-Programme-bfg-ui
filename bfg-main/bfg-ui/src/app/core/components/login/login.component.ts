import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoading = false;
  errorMessage: string;
  credentials = {
    username: '',
    password: '',
  };

  constructor() { }

  ngOnInit(): void {
  }

  login() {
    this.errorMessage = 'Error message';
  }

}
