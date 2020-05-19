import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { INBOUND_REQUEST_TYPES } from '../inbound-request-types';
import { ENTITY_VALIDATION_MESSAGES } from '../entity-validation-messages';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ErrorMessage } from 'src/app/core/utils/error-template';
import { get } from 'lodash';
import { Observable } from 'rxjs';
import { Entity } from 'src/app/shared/entity/entity.model';

@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  isLinear = true;

  @ViewChild('stepper') stepper;

  inboundRequestTypeList: string[] = INBOUND_REQUEST_TYPES;
  validationMessages = ENTITY_VALIDATION_MESSAGES;
  errorMessage: ErrorMessage;

  saving: boolean;
  saving$: Observable<Entity>;

  summaryDisplayedColumns = ['field', 'value', 'error'];
  summaryPageDataSource;

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private entityService: EntityService
  ) { }

  ngOnInit() {
    this.initializeFormGroups();
    this.getSummaryFieldsSource();
  }

  initializeFormGroups() {
    this.entityTypeFormGroup = this.formBuilder.group({
      service: ['', Validators.required]
    });
    this.entityPageFormGroup = this.formBuilder.group({
      entity: ['', Validators.required],
      routeInbound: [true, Validators.required],
      inboundRequestorDN: ['', Validators.required],
      inboundResponderDN: ['', Validators.required],
      inboundService: ['swift.corp.fa', Validators.required],
      inboundRequestType: [[]],
      inboundDir: [true, Validators.required],
      inboundRoutingRule: [true, Validators.required]
    });
    this.SWIFTDetailsFormGroup = this.formBuilder.group({
      requestorDN: ['', Validators.required],
      responderDN: ['', Validators.required],
      requestType: [],
      trace: [],
      snF: [],
      deliveryNotification: [],
      nonRepudiation: [],
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
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Create ${entityName} entity`,
      text: `Are you sure to create ${entityName} entity?`,
      yesCaption: 'Create',
      noCaption: 'Cancel'
    }));
    dialogRef.afterClosed().subscribe(result => {
      this.errorMessage = null;
      this.getSummaryFieldsSource();
      if (result) {
        const entity = removeEmpties({
          ...this.entityTypeFormGroup.value,
          ...this.entityPageFormGroup.value,
          ...this.SWIFTDetailsFormGroup.value,
          ...this.summaryPageFormGroup.value
        });
        this.saving$ = this.entityService.createEntity(entity);
        this.saving$.subscribe(
          () => {
            this.stepper.reset();
            this.initializeFormGroups();
          },
          (error) => {
            this.errorMessage = error;
            this.getSummaryFieldsSource();
            this.markAllFieldsTouched();
          }
        );
      }
    });
  }

  cancelCreationEntity() {
    const entityName = this.entityTypeFormGroup.get('service').value || 'new';
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Cancel creation of the ${entityName} entity`,
      text: `Are you sure to cancel the creation of the ${entityName} entity?`,
      yesCaption: 'Reset',
      yesCaptionColor: 'warn',
      noCaption: 'Cancel'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.errorMessage = null;
        this.getSummaryFieldsSource();
        this.stepper.reset();
        this.initializeFormGroups();
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
}
