import { Component, OnInit, ViewChild, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import { TrustedCertificateServiceService } from 'src/app/shared/models/trustedCertificate/trusted-certificate-service.service';
import { FormBuilder, FormGroup, Validators, FormGroupDirective } from '@angular/forms';
import { getTrustedCertificateDisplayName } from '../trusted-certificate-display-names';
import { TRUSTED_CERTIFICATE_VALIDATION_MESSAGES } from '../validation-messages';
import { TrustedCertificate, TSItemInfo } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { get } from 'lodash';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { removeEmpties } from 'src/app/shared/utils/utils';

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

  confirmationDisplayedColumns = ['field', 'value', 'error'];
  confirmationPageDataSource;

  @ViewChild('stepper') stepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  uploadTrustedCertificateFormGroup: FormGroup;
  detailsTrustedCertificateFormGroup: FormGroup;
  confirmationTrustedCertificateFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private trustedCertificateService: TrustedCertificateServiceService,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.uploadTrustedCertificateFormGroup = this.formBuilder.group({
      trustedCertificateFile: ['', Validators.required]
    });
    this.initializeDetailsFormGroups(this.getTrustedCertificateDefaultValue());
  }

  initializeDetailsFormGroups(trustedCertificate: TrustedCertificate) {
    this.detailsTrustedCertificateFormGroup = this.formBuilder.group({
      name: [get(this.trustedCertificateFile, 'name')],
      serialNumber: [trustedCertificate.serialNumber],
      thumbprint: [trustedCertificate.thumbprint],
      validDates: [trustedCertificate.startDate && trustedCertificate.endDate && `${trustedCertificate.startDate}-${trustedCertificate.endDate}`],
      issuer: [trustedCertificate.issuer],
      subject: [trustedCertificate.subject],
      authChainReport: [trustedCertificate.authChainReport],
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
    this.trustedCertificateFile = files.item(0);
    const formData: FormData = new FormData();
    formData.append('file', this.trustedCertificateFile, this.trustedCertificateFile.name);
    this.trustedCertificateService.uploadTrustedCertificate(formData).pipe(data => this.setLoading(data))
      .subscribe((data: TrustedCertificate) => {
        this.isLoading = false;
        this.initializeDetailsFormGroups(data);
        this.stepper.next();
      }, error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
        this.uploadTrustedCertificateFormGroup.get('trustedCertificateFile').setValue(null);
      });
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);

  getTSItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`);

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
      authChainReport: get(this.detailsTrustedCertificateFormGroup.get('authChainReport'), 'value', [])
        .map(el => this.getTSItemInfoValues(el).join(',\n')),
      issuer: this.getTSItemInfoValues(get(this.detailsTrustedCertificateFormGroup.get('issuer'), 'value', {})),
      subject: this.getTSItemInfoValues(get(this.detailsTrustedCertificateFormGroup.get('subject'), 'value', {})),
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
      formData.append('file', this.trustedCertificateFile, this.trustedCertificateFile.name);
      formData.append('name', this.detailsTrustedCertificateFormGroup.get('name').value);
      formData.append('comments', this.detailsTrustedCertificateFormGroup.get('changerComments').value);
      if (result) {
        this.trustedCertificateService.uploadTrustedCertificate(formData).pipe(data => this.setLoading(data)).subscribe(
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
