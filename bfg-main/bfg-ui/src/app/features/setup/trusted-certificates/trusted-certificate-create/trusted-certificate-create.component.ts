import { Component, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { entries, get, isArray } from 'lodash';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { TRUSTED_CERTIFICATE_NAME } from 'src/app/core/constants/validation-regexes';
import { ErrorMessage, getApiErrorMessage, getErrorByField, getErrorsMessage } from 'src/app/core/utils/error-template';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { TrustedCertificateValidators } from 'src/app/shared/models/trustedCertificate/trusted-certificate-validator';
import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { TrustedCertificateService } from 'src/app/shared/models/trustedCertificate/trusted-certificate.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import {
  getTrustedCertificateDisplayName,
  getTrustedCertificateItemInfoValues,
  getTrustedCertificateItemInfoValuesOrdered,
  getValidityLabel
} from '../trusted-certificate-display-names';
import { TRUSTED_CERTIFICATE_VALIDATION_MESSAGES } from '../validation-messages';

@Component({
  selector: 'app-trusted-certificate-create',
  templateUrl: './trusted-certificate-create.component.html',
  styleUrls: ['./trusted-certificate-create.component.scss']
})
export class TrustedCertificateCreateComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;
  getTrustedCertificateDisplayName = getTrustedCertificateDisplayName;
  getValidityLabel = getValidityLabel;
  getTrustedCertificateItemInfoValues = getTrustedCertificateItemInfoValues;
  getTrustedCertificateItemInfoValuesOrdered = getTrustedCertificateItemInfoValuesOrdered;

  trustedCertificateValidationMessages = TRUSTED_CERTIFICATE_VALIDATION_MESSAGES;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  trustedCertificateFile;

  confirmationDisplayedColumns = ['field', 'sub-field', 'value'];
  confirmationPageDataSource = [];

  @ViewChild('stepper') stepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  uploadTrustedCertificateFormGroup: FormGroup;
  detailsTrustedCertificateFormGroup: FormGroup;
  confirmationTrustedCertificateFormGroup: FormGroup;

  editableTrustedCertificate: TrustedCertificate;
  pendingChange: ChangeControl;
  changeId: string;

  constructor(
    private formBuilder: FormBuilder,
    private trustedCertificateService: TrustedCertificateService,
    private dialog: MatDialog,
    private trustedCertificateValidators: TrustedCertificateValidators,
    private toolTip: TooltipService,
    private activatedRouter: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
    this.uploadTrustedCertificateFormGroup = this.formBuilder.group({
      trustedCertificateFile: ['', Validators.required]
    });
    this.initializeDetailsFormGroups(this.getTrustedCertificateDefaultValue());
    this.activatedRouter.params.subscribe(params => {
      if (params.changeId) {
        this.changeId = params.changeId;
        this.trustedCertificateService.getPendingChangeById(params.changeId)
          .pipe(data => this.setLoading(data)).subscribe((data: ChangeControl) => {
            this.isLoading = false;
            this.pendingChange = data;
            this.validateTrustedCertificateById({
              ...this.pendingChange.trustedCertificateLog,
              changerComments: this.pendingChange.changerComments
            });
          },
            error => {
              this.isLoading = false;
              this.errorMessage = getApiErrorMessage(error);
            });
      }
    });
  }

  validateTrustedCertificateById = (certificate: TrustedCertificate) => {
    let validateCertificate;
    if (certificate.certificateId) {
      validateCertificate = this.trustedCertificateService.validateCertificateById(certificate.certificateId);
    }
    else {
      validateCertificate = this.trustedCertificateService.validateCertificateLogById(certificate.certificateLogId);
    }
    validateCertificate
      .pipe(data => this.setLoading(data))
      .subscribe((data: TrustedCertificate) => {
        this.isLoading = false;
        this.initializeDetailsFormGroups({
          ...data,
          certificateName: certificate.certificateName,
          changerComments: certificate.changerComments
        });
        if (data.certificateErrors || data.certificateWarnings) {
          this.errorMessage = this.getErrorsAndWarnings(data);
          this.notificationService.showErrorWithWarningMessage(this.errorMessage);
        }
        if (this.pendingChange && !(this.tryToProceedPendingEdit())) {
          this.detailsTrustedCertificateFormGroup.addControl('disableProceed', new FormControl('', [Validators.required]));
        }
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
          this.uploadTrustedCertificateFormGroup.get('trustedCertificateFile').setErrors({ invalid: true });
        });
  }

  initializeDetailsFormGroups(trustedCertificate: TrustedCertificate) {
    this.detailsTrustedCertificateFormGroup = this.formBuilder.group({
      name: [trustedCertificate.certificateName || this.getTrustedCertificateName(trustedCertificate), {
        validators: [
          Validators.required,
          Validators.pattern(TRUSTED_CERTIFICATE_NAME)
        ],
        asyncValidators:
          this.trustedCertificateValidators.trustedCertificateExistsValidator(),
        updateOn: 'blur'
      }],
      serialNumber: [trustedCertificate.serialNumber],
      thumbprint: [trustedCertificate.thumbprint],
      thumbprint256: [trustedCertificate.thumbprint256],
      validDates: [trustedCertificate.from && trustedCertificate.endDate && `${trustedCertificate.from}-${trustedCertificate.endDate}`],
      issuer: [trustedCertificate.issuer],
      subject: [trustedCertificate.subject],
      authChainReport: [trustedCertificate.authChainReport],
      changerComments: [trustedCertificate.changerComments],
      valid: [trustedCertificate.valid],
    }, { validators: this.trustedCertificateValidators.isTrustedCertificateHasErrors(trustedCertificate) });
  }

  getTrustedCertificateName = (trustedCertificate: TrustedCertificate) => get(trustedCertificate, 'subject.O', '').toString() !== ''
    ? `${get(trustedCertificate, 'subject.O', '')}-${trustedCertificate.serialNumber}` : trustedCertificate.serialNumber

  getTrustedCertificateDefaultValue = (): TrustedCertificate => ({
    certificateName: '',
    serialNumber: '',
    thumbprint: '',
    thumbprint256: '',
    from: null,
    endDate: null,
    issuer: {
      C: [],
      CN: [],
      L: [],
      O: [],
      OU: [],
      ST: [],
      EMAILADDRESS: []
    },
    subject: {
      C: [],
      CN: [],
      L: [],
      O: [],
      OU: [],
      ST: [],
      EMAILADDRESS: []
    },
    authChainReport: [],
    valid: false,
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
        if (data.certificateErrors || data.certificateWarnings) {
          this.errorMessage = this.getErrorsAndWarnings(data);
          this.notificationService.showErrorWithWarningMessage(this.errorMessage);
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
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);

  cancelTrustedCertificate() {
    const trustedCertificateName = this.detailsTrustedCertificateFormGroup.get('name').value || 'new';
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Cancel ${this.changeId ? 'editing' : 'creation'} of the ${trustedCertificateName} Trusted Certificate`,
      text: `Are you sure to cancel the  ${this.changeId ? 'editing' : 'creation'} of the ${trustedCertificateName} Trusted Certificate ? `,
      yesCaption: `Cancel  ${this.changeId ? 'editing' : 'creation'}`,
      yesCaptionColor: 'warn',
      noCaption: 'Back'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (this.changeId) {
          this.goToPreviousPage();
        } else {
          this.errorMessage = null;
          this.stepper.reset();
          this.resetAllForms();
        }
      }
    });
  }

  goToPreviousPage = () => {
    const previousURL = get(window.history.state, 'previousURL');
    if (previousURL) {
      this.router.navigate([previousURL], { state: window.history.state });
    } else {
      this.router.navigate(['/' + ROUTING_PATHS.TRUSTED_CERTIFICATES + '/' + ROUTING_PATHS.PENDING], { state: window.history.state });
    }
  }

  resetAllForms() {
    this.formGroups.forEach(formGroup => formGroup.resetForm());
  }

  getConfirmationFieldsSource() {
    const trustedCertificate = removeEmpties({
      ...this.detailsTrustedCertificateFormGroup.value,
      issuer: getTrustedCertificateItemInfoValuesOrdered(get(this.detailsTrustedCertificateFormGroup.get('issuer'), 'value', {})),
      subject: getTrustedCertificateItemInfoValuesOrdered(get(this.detailsTrustedCertificateFormGroup.get('subject'), 'value', {})),
      valid: this.getValidityMessage() || getValidityLabel(this.detailsTrustedCertificateFormGroup.get('valid').value),
      authChainReport: get(this.detailsTrustedCertificateFormGroup.get('authChainReport'), 'value', []) || [],
    });

    const data = [];

    entries(trustedCertificate).forEach(([field, value]) => {
      if (isArray(value) && field !== 'authChainReport') {
        value.forEach((val, index) => {
          data.push({
            key: val,
            field: index === 0 ? `${getTrustedCertificateDisplayName(field)}:` : '',
            subField: val.split(': ')[0],
            value: val.split(': ')[1],
          });
        });
      } else if (isArray(value) && field === 'authChainReport') {
        value.forEach((current, outIndex) => {
          entries(current).forEach(([key, val], inIndex) => {
            data.push({
              field: outIndex === 0 && inIndex === 0 ? `${getTrustedCertificateDisplayName('authChainReport')}:` : undefined,
              subField: getTrustedCertificateDisplayName(key),
              value: val,
            });
          });
        });
      } else {
        data.push({
          key: field,
          field: getTrustedCertificateDisplayName(field),
          value,
        });
      }
    });

    this.confirmationPageDataSource = data;
  }

  sendTrustedCertificate() {
    const trustedCertificateName = this.detailsTrustedCertificateFormGroup.get('name').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Save ${trustedCertificateName} trusted certificate`,
      text: `Are you sure to save ${trustedCertificateName} trusted certificate ? `,
      yesCaption: 'Submit',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      if (result) {
        let sendTrustedCertificate = null;
        if (this.pendingChange) {
          sendTrustedCertificate = this.trustedCertificateService.editPendingChange(this.pendingChange.changeID,
            {
              name: this.detailsTrustedCertificateFormGroup.get('name').value,
              comments: this.detailsTrustedCertificateFormGroup.get('changerComments').value
            });
        } else {
          const formData: FormData = new FormData();
          formData.append('file', this.trustedCertificateFile, this.trustedCertificateFile.name);
          formData.append('name', this.detailsTrustedCertificateFormGroup.get('name').value);
          formData.append('comments', this.detailsTrustedCertificateFormGroup.get('changerComments').value || '');
          sendTrustedCertificate = this.trustedCertificateService.createTrustedCertificate(formData);
        }
        sendTrustedCertificate.pipe(data => this.setLoading(data)).subscribe(
          () => {
            this.isLoading = false;
            this.notificationService.show(
              'Trusted Certificate  saved',
              `Trusted Certificate ${trustedCertificateName} has been saved`,
              'success'
            );
            if (this.changeId) {
              this.goToPreviousPage();
            } else {
              this.stepper.reset();
              this.resetAllForms();
            }
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

  hasWarnings(): boolean {
    return this.errorMessage && this.errorMessage.warnings && this.errorMessage.warnings.length > 0;
  }

  getValidityMessage(): string {
    let validityMessage = null;
    if (this.hasWarnings()) {
      validityMessage = this.getErrorsMessage(this.errorMessage.warnings[0])[0];
    }
    return validityMessage;
  }

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'trusted-cert',
      qualifier: step,
      mode: 'create',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getTrustedCertificateDisplayName(field);
  }

  tryToProceed() {
    if (this.tryToProceedPendingEdit()) {
      this.stepper.next();
    } else {
      this.authService.showForbidden();
    }
  }

  tryToProceedPendingEdit = () => this.pendingChange
    ? (this.pendingChange.operation !== CHANGE_OPERATION.DELETE
      && this.authService.isTheSameUser(this.pendingChange.changer)) : true
}
