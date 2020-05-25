import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { INBOUND_REQUEST_TYPES } from '../inbound-request-types';
import { ENTITY_VALIDATION_MESSAGES } from '../entity-validation-messages';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { get } from 'lodash';
import { ENTITY_DISPLAY_NAMES } from '../entity-display-names';
import { EntityValidators } from '../../../../shared/entity/entity-validators';

@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  entityDisplayNames = ENTITY_DISPLAY_NAMES;
  isLinear = true;

  @ViewChild('stepper') stepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  inboundRequestTypeList: string[] = INBOUND_REQUEST_TYPES;
  validationMessages = ENTITY_VALIDATION_MESSAGES;
  errorMessage: ErrorMessage;

  isLoading = false;

  summaryDisplayedColumns = ['field', 'value', 'error'];
  summaryPageDataSource;

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private entityService: EntityService,
    private entityValidators: EntityValidators
  ) { }

  ngOnInit() {
    this.initializeFormGroups();
  }

  initializeFormGroups() {
    this.entityTypeFormGroup = this.formBuilder.group({
      service: ['', Validators.required]
    });
    this.entityPageFormGroup = this.formBuilder.group({
      entity: ['', {
        validators: [
          Validators.required,
          this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
          ],
        asyncValidators: this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
        updateOn: 'blur'}],
      routeInbound: [true, Validators.required],
      inboundRequestorDN: ['', Validators.required],
      inboundResponderDN: ['', Validators.required],
      inboundService: ['swift.corp.fa', Validators.required],
      inboundRequestType: [],
      inboundDir: [true, Validators.required],
      inboundRoutingRule: [true, Validators.required]
    });
    this.SWIFTDetailsFormGroup = this.formBuilder.group({
      requestorDN: ['', Validators.required],
      responderDN: ['', Validators.required],
      requestType: [],
      trace: [false],
      snF: [false],
      deliveryNotification: [false],
      nonRepudiation: [false],
      e2eSigning: ['None', Validators.required],
      deliveryNotifDN: [],
      deliveryNotifRT: [],
      requestRef: [],
      fileInfo: [],
      fileDesc: [],
      transferInfo: [],
      transferDesc: []
    });

    this.summaryPageFormGroup = this.formBuilder.group({
      changerComments: [, Validators.nullValidator]
    });
  }

  onInboundRequestTypeRemoved(inboundRequestType: string) {
    const inboundRequestTypeList = this.entityPageFormGroup.get('inboundRequestType').value as string[];
    this.removeFirst(inboundRequestTypeList, inboundRequestType);
    this.entityPageFormGroup.get('inboundRequestType').setValue(inboundRequestTypeList);
  }

  private removeFirst<T>(array: T[], toRemove: T): void {
    const index = array.indexOf(toRemove);
    if (index !== -1) {
      array.splice(index, 1);
    }
  }

  createEntity() {
    const entityName = this.entityTypeFormGroup.get('service').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Create ${entityName} entity`,
      text: `Are you sure to create ${entityName} entity?`,
      yesCaption: 'Create',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      this.errorMessage = null;
      if (result) {
        const entity = removeEmpties({
          ...this.entityTypeFormGroup.value,
          ...this.entityPageFormGroup.value,
          ...this.SWIFTDetailsFormGroup.value,
          ...this.summaryPageFormGroup.value
        });
        this.entityService.createEntity(entity).pipe(data => this.setLoading(data)).subscribe(
          () => {
            this.isLoading = false;
            this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
              title: `Entity created`,
              text: `Entity ${entityName} has been created`,
              shouldHideYesCaption: true,
              noCaption: 'Back'
            })).afterClosed().subscribe(() => {
              this.stepper.reset();
              this.initializeFormGroups();
              this.resetAllForms();
            });
          },
          (error) => {
            this.isLoading = false;
            this.errorMessage = getApiErrorMessage(error);
            this.getSummaryFieldsSource();
            this.markAllFieldsTouched();
          }
        );
      }
    });
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  cancelCreationEntity() {
    const entityName = this.entityTypeFormGroup.get('service').value || 'new';
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Cancel creation of the ${entityName} entity`,
      text: `Are you sure to cancel the creation of the ${entityName} entity?`,
      yesCaption: 'Cancel creation',
      yesCaptionColor: 'warn',
      noCaption: 'Back'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.errorMessage = null;
        this.stepper.reset();
        this.initializeFormGroups();
        this.resetAllForms();
      }
    });
  }

  getErrorByField(key) {
    return get(this.errorMessage, 'errors', []).filter(el => el[key]).map(el => el[key]).join('\n ');
  }

  getSummaryFieldsSource() {
    const entity = removeEmpties({
      ...this.entityTypeFormGroup.value,
      ...this.entityPageFormGroup.value,
      ...this.SWIFTDetailsFormGroup.value,
      ...this.summaryPageFormGroup.value
    });
    this.summaryPageDataSource = Object.keys(entity)
      .map((key) => ({
        field: key,
        value: entity[key],
        error: this.getErrorByField(key)
      }));
  }

  markAllFieldsTouched() {
    this.entityTypeFormGroup.markAllAsTouched();
    this.entityPageFormGroup.markAllAsTouched();
    this.SWIFTDetailsFormGroup.markAllAsTouched();
    this.summaryPageFormGroup.markAllAsTouched();
  }

  resetRadioButton(formGroup: FormGroup, fieldName) {
    formGroup.get(fieldName).reset();
  }

  resetAllForms() {
    this.formGroups.forEach(formGroup => formGroup.resetForm());
  }
}
