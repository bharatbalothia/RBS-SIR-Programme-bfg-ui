import { Component, OnInit } from '@angular/core';
import { ErrorMessage, getErrorsMessage } from 'src/app/core/utils/error-template';

@Component({
  selector: 'app-trusted-certificate-create',
  templateUrl: './trusted-certificate-create.component.html',
  styleUrls: ['./trusted-certificate-create.component.scss']
})
export class TrustedCertificateCreateComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;

  errorMessage: ErrorMessage;
  isLoading = false;

  constructor() { }

  ngOnInit(): void {
  }

}
