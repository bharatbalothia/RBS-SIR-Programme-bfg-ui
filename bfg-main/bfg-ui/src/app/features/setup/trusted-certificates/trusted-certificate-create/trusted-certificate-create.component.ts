import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import { TrustedCertificateServiceService } from 'src/app/shared/models/trustedCertificate/trusted-certificate-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { getTrustedCertificateDisplayName } from '../trusted-certificate-display-names';
import { TRUSTED_CERTIFICATE_VALIDATION_MESSAGES } from '../validation-messages';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { get } from 'lodash';

@Component({
  selector: 'app-trusted-certificate-create',
  templateUrl: './trusted-certificate-create.component.html',
  styleUrls: ['./trusted-certificate-create.component.scss']
})
export class TrustedCertificateCreateComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;
  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  trustedCertificateValidationMessages = TRUSTED_CERTIFICATE_VALIDATION_MESSAGES;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  trustedCertificateFile;

  @ViewChild('stepper') stepper;

  uploadTrustedCertificateFormGroup: FormGroup;
  detailsTrustedCertificateFormGroup: FormGroup;
  confirmationTrustedCertificateFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private trustedCertificateService: TrustedCertificateServiceService
  ) { }

  ngOnInit(): void {
    this.initializeFormGroups(this.getTrustedCertificateDefaultValue());
  }

  initializeFormGroups(trustedCertificate: TrustedCertificate) {
    this.uploadTrustedCertificateFormGroup = this.formBuilder.group({
      trustedCertificateFile: ['', Validators.required]
    });
    this.detailsTrustedCertificateFormGroup = this.formBuilder.group({
      name: [get(this.trustedCertificateFile, 'name')],
      serialNumber: [''],
      thumbprint: [],
      startDate: [trustedCertificate.startDate && trustedCertificate.endDate && `${trustedCertificate.startDate}-${trustedCertificate.endDate}`],
      issuerC: [],
      issuerCN: [],
      issuerL: [],
      issuerO: [],
      issuerOU: [],
      subjectC: [],
      subjectCN: [],
      subjectL: [],
      subjectO: [],
      subjectST: [],
    });
    this.confirmationTrustedCertificateFormGroup = this.formBuilder.group({
      changerComments: ['']
    });
  }

  getTrustedCertificateDefaultValue = (): TrustedCertificate => ({
    serialNumber: '',
    thumbprint: '',
    startDate: null,
    endDate: null,
    issuer: {
      C: [],
      CN: [],
      L: [],
      O: [],
      OU: []
    },
    subject: {
      C: [],
      CN: [],
      L: [],
      O: [],
      ST: [],
    },
    authChainReport: [],
    valid: null,
    changerComments: ''
  })

  handleFileInput(files: FileList) {
    this.trustedCertificateFile = files.item(0);
    const formData: FormData = new FormData();
    formData.append('file', this.trustedCertificateFile, this.trustedCertificateFile.name);
    this.trustedCertificateService.uploadTrustedCertificate(formData).pipe(data => this.setLoading(data))
      .subscribe((data: TrustedCertificate) => {
        this.isLoading = false;
        this.initializeFormGroups(data);
        this.confirmationTrustedCertificateFormGroup = this.formBuilder.group({});
        this.stepper.next();
      }, error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);
}
