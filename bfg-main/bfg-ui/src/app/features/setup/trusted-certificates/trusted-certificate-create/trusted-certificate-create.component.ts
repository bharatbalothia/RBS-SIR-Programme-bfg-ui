import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { FormBuilder, FormGroup, Validators, FormGroupDirective } from '@angular/forms';
import { getTrustedCertificateDisplayName, getTrustedCertificateItemInfoValues } from '../trusted-certificate-display-names';
import { TRUSTED_CERTIFICATE_VALIDATION_MESSAGES } from '../validation-messages';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { get } from 'lodash';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { TRUSTED_CERTIFICATE_NAME } from 'src/app/core/constants/validation-regexes';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { TrustedCertificateValidators } from 'src/app/shared/models/trustedCertificate/trusted-certificate-validator';

@Component({
  selector: 'app-trusted-certificate-create',
  templateUrl: './trusted-certificate-create.component.html',
  styleUrls: ['./trusted-certificate-create.component.scss']
})
export class TrustedCertificateCreateComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;
  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  getTrustedCertificateItemInfoValues = getTrustedCertificateItemInfoValues;

  trustedCertificateValidationMessages = TRUSTED_CERTIFICATE_VALIDATION_MESSAGES;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  trustedCertificateFile;

  confirmationDisplayedColumns = ['field', 'value', 'error'];
  confirmationPageDataSource;

  @ViewChild('stepper') stepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  uploadTrustedCertificateFormGroup: FormGroup;
  detailsTrustedCertificateFormGroup: FormGroup;
  confirmationTrustedCertificateFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private trustedCertificateService: TrustedCertificateService,
    private dialog: MatDialog,
    private trustedCertificateValidators: TrustedCertificateValidators,
  ) { }

  ngOnInit(): void {
    this.uploadTrustedCertificateFormGroup = this.formBuilder.group({
      trustedCertificateFile: ['', Validators.required]
    });
    this.initializeDetailsFormGroups(this.getTrustedCertificateDefaultValue());
  }

  initializeDetailsFormGroups(trustedCertificate: TrustedCertificate) {
    this.detailsTrustedCertificateFormGroup = this.formBuilder.group({
      name: [this.getTrustedCertificateName(trustedCertificate), {
        validators: [
          Validators.required,
          Validators.pattern(TRUSTED_CERTIFICATE_NAME)
        ]
      }],
      serialNumber: [trustedCertificate.serialNumber],
      thumbprint: [trustedCertificate.thumbprint],
      thumbprint256: [trustedCertificate.thumbprint256],
      validDates: [trustedCertificate.startDate && trustedCertificate.endDate && `${trustedCertificate.startDate}-${trustedCertificate.endDate}`],
      issuer: [trustedCertificate.issuer],
      subject: [trustedCertificate.subject],
      authChainReport: [trustedCertificate.authChainReport],
      changerComments: ['']
    }, { validators: this.trustedCertificateValidators.isTrustedCertificateHasErrors(trustedCertificate) });
  }

  getTrustedCertificateName = (trustedCertificate: TrustedCertificate) => get(trustedCertificate, 'subject.O', '').toString() !== ''
    ? `${get(trustedCertificate, 'subject.O', '')}-${trustedCertificate.serialNumber}` : trustedCertificate.serialNumber

  getTrustedCertificateDefaultValue = (): TrustedCertificate => ({
    certificateName: '',
    serialNumber: '',
    thumbprint: '',
    thumbprint256: '',
    startDate: null,
    endDate: null,
    issuer: {
      C: [],
      CN: [],
      L: [],
      O: [],
      OU: [],
      ST: [],
    },
    subject: {
      C: [],
      CN: [],
      L: [],
      O: [],
      OU: [],
      ST: [],
    },
    authChainReport: [],
    valid: null,
    changerComments: ''
  })

  handleFileInput(files: FileList) {
    this.errorMessage = null;
    this.trustedCertificateFile = files.item(0);
    const formData: FormData = new FormData();
    formData.append('file', this.trustedCertificateFile);
    this.trustedCertificateService.uploadTrustedCertificate(formData).pipe(data => this.setLoading(data))
      .subscribe((data: TrustedCertificate) => {
        this.isLoading = false;
        this.initializeDetailsFormGroups(data);
        this.stepper.next();
        if (data.certificateErrors || data.certificateWarnings) {
          this.errorMessage = this.getErrorsAndWarnings(data);
        }
      }, error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
        this.uploadTrustedCertificateFormGroup.get('trustedCertificateFile').setErrors({ invalid: true });
      });
  }

  getErrorsAndWarnings = (trustedCertificate: TrustedCertificate) => removeEmpties({
    code: null,
    message: trustedCertificate.certificateErrors && ERROR_MESSAGES.trustedCertificateErrors,
    errors: get(trustedCertificate, 'certificateErrors', null),
    warnings: get(trustedCertificate, 'certificateWarnings', null)
  })

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);

  cancelTrustedCertificate() {
    const trustedCertificateName = this.detailsTrustedCertificateFormGroup.get('name').value || 'new';
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Cancel creation of the ${trustedCertificateName} Trusted Certificate`,
      text: `Are you sure to cancel the creation of the ${trustedCertificateName} Trusted Certificate?`,
      yesCaption: `Cancel creation`,
      yesCaptionColor: 'warn',
      noCaption: 'Back'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.errorMessage = null;
        this.stepper.reset();
        this.resetAllForms();
      }
    });
  }

  resetAllForms() {
    this.formGroups.forEach(formGroup => formGroup.resetForm());
  }

  getConfirmationFieldsSource() {
    const entity = removeEmpties({
      ...this.detailsTrustedCertificateFormGroup.value,
      authChainReport: (get(this.detailsTrustedCertificateFormGroup.get('authChainReport'), 'value', []) || [])
        .map(el => getTrustedCertificateItemInfoValues(el).join(',\n')),
      issuer: getTrustedCertificateItemInfoValues(get(this.detailsTrustedCertificateFormGroup.get('issuer'), 'value', {})),
      subject: getTrustedCertificateItemInfoValues(get(this.detailsTrustedCertificateFormGroup.get('subject'), 'value', {})),
    });
    this.confirmationPageDataSource = Object.keys(entity)
      .map((key) => ({
        field: key,
        value: entity[key],
        error: getErrorByField(key, this.errorMessage)
      }));
  }

  sendTrustedCertificate() {
    const trustedCertificateName = this.detailsTrustedCertificateFormGroup.get('name').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Create ${trustedCertificateName} trusted certificate`,
      text: `Are you sure to create ${trustedCertificateName} trusted certificate?`,
      yesCaption: 'Create',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      this.errorMessage = null;
      const formData: FormData = new FormData();
      formData.append('file', this.trustedCertificateFile);
      formData.append('name', this.detailsTrustedCertificateFormGroup.get('name').value);
      formData.append('comments', this.detailsTrustedCertificateFormGroup.get('changerComments').value);
      if (result) {
        this.trustedCertificateService.createTrustedCertificate(formData).pipe(data => this.setLoading(data)).subscribe(
          () => {
            this.isLoading = false;
            this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
              title: `Trusted Certificate created`,
              text: `Trusted Certificate ${trustedCertificateName} has been created`,
              shouldHideYesCaption: true,
              noCaption: 'Back'
            })).afterClosed().subscribe(() => {
              this.stepper.reset();
              this.resetAllForms();
            });
          },
          (error) => {
            this.isLoading = false;
            this.errorMessage = getApiErrorMessage(error);
            this.getConfirmationFieldsSource();
          }
        );
      }
    });
  }
}
