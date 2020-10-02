import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ENTITY_VALIDATION_MESSAGES } from '../validation-messages';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ErrorMessage, getApiErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import { get, isEmpty } from 'lodash';
import { ENTITY_DISPLAY_NAMES } from '../entity-display-names';
import { EntityValidators } from '../../../../shared/models/entity/entity-validators';
import { SWIFT_DN, TIME_24, NON_NEGATIVE_INT } from 'src/app/core/constants/validation-regexes';
import { ActivatedRoute, Router } from '@angular/router';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { Observable } from 'rxjs';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-constants';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { Schedule } from 'src/app/shared/models/schedule/schedule.model';
import { EntityScheduleDialogComponent } from '../entity-schedule-dialog/entity-schedule-dialog.component';
import { EntityScheduleDialogConfig } from '../entity-schedule-dialog/entity-schedule-dialog-config.model';
import { MatTableDataSource } from '@angular/material/table';
import { MQDetails } from 'src/app/shared/models/entity/mq-details.model';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';

@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  get = get;

  entityDisplayNames = ENTITY_DISPLAY_NAMES;
  scheduleType = SCHEDULE_TYPE;

  isLinear = true;

  @ViewChild('stepper') stepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  inboundRequestTypeList: string[] = [];
  entityValidationMessages = ENTITY_VALIDATION_MESSAGES;
  errorMessage: ErrorMessage;

  isLoading = false;

  summaryDisplayedColumns = ['field', 'value', 'error'];
  summaryPageDataSource;

  scheduleDisplayedColumns = ['action', 'schedule', 'scheduleType'];
  schedulesDataSource;
  scheduleFileTypes: string[] = [];

  mqDetails: MQDetails = {
    CaDigitalCertificates: [],
    Debug: [],
    QueueBinding: [],
    QueueContext: [],
    SSLCiphers: [],
    SSLOptions: [],
    SystemDigitalCertificates: []
  };

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;
  schedulesFormGroup: FormGroup;
  mqDetailsFormGroup: FormGroup;

  editableEntity: Entity;
  selectedService = '';

  requiredFields: { [filedName: string]: boolean } = {};

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private entityService: EntityService,
    private entityValidators: EntityValidators,
    private activatedRouter: ActivatedRoute,
    private router: Router,
    private toolTip: TooltipService
  ) { }

  ngOnInit() {
    this.initializeFormGroups(this.getEntityDefaultValue());
    this.activatedRouter.params.subscribe(params => {
      if (params.entityId) {
        this.entityService.getEntityById(params.entityId).pipe(data => this.setLoading(data)).subscribe((data: Entity) => {
          this.isLoading = false;
          this.editableEntity = data;
          this.onServiceSelect(this.editableEntity.service.toUpperCase(), this.editableEntity);
          this.markAllFieldsTouched();
        },
          error => {
            this.isLoading = false;
            this.errorMessage = getApiErrorMessage(error);
          });
      }
    });
  }

  initializeFormGroups(entity: Entity) {
    this.entityTypeFormGroup = this.formBuilder.group({
      service: [entity.service, Validators.required]
    });
    this.entityPageFormGroup = this.formBuilder.group({});
    this.SWIFTDetailsFormGroup = this.formBuilder.group({
      requestorDN: [entity.requestorDN, {
        validators: [
          Validators.pattern(SWIFT_DN)
        ]
      }],
      responderDN: [entity.responderDN, {
        validators: [
          Validators.pattern(SWIFT_DN)
        ]
      }],
      serviceName: [entity.serviceName],
      requestType: [entity.requestType],
      trace: [entity.trace || false],
      snF: [entity.snF || false],
      deliveryNotification: [entity.deliveryNotification || false],
      nonRepudiation: [entity.nonRepudiation || false],
      e2eSigning: [entity.e2eSigning, Validators.required],
      deliveryNotifDN: [entity.deliveryNotifDN],
      deliveryNotifRT: [entity.deliveryNotifRT],
      requestRef: [entity.requestRef],
      fileInfo: [entity.fileInfo],
      fileDesc: [entity.fileDesc],
      transferInfo: [entity.transferInfo],
      transferDesc: [entity.transferDesc]
    });
    this.summaryPageFormGroup = this.formBuilder.group({
      changerComments: [entity.changerComments, Validators.nullValidator]
    });
  }

  getEntityDefaultValue = (): Entity => ({
    service: '',
    entity: '',
    routeInbound: true,
    inboundRequestorDN: '',
    inboundResponderDN: '',
    inboundService: 'swift.corp.fa',
    inboundDir: true,
    inboundRoutingRule: true,
    requestorDN: '',
    responderDN: '',
    serviceName: '',
    trace: false,
    snF: false,
    deliveryNotification: false,
    nonRepudiation: false,
    e2eSigning: 'None',
    mailboxPathIn: '',
    mailboxPathOut: '',
    maxBulksPerFile: null,
    maxTransfersPerBulk: null,
    endOfDay: null,
    startOfDay: null,
    compression: false,
    entityParticipantType: 'INDIRECT'
  })

  onServiceSelect(value, entity: Entity = this.getEntityDefaultValue()) {
    this.selectedService = value;
    this.formGroups.forEach(formGroup => !formGroup.control.get('service') && formGroup.resetForm());
    this.initializeFormGroups({ ...entity, service: value });
    switch (value) {
      case ENTITY_SERVICE_TYPE.SCT:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [{ value: entity.entity, disabled: this.isEditing() }, {
            validators: [
              Validators.required,
              this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
            ],
            asyncValidators: !this.isEditing() && this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
            updateOn: 'blur'
          }],
          maxBulksPerFile: [entity.maxBulksPerFile, {
            validators: [
              Validators.required,
              Validators.pattern(NON_NEGATIVE_INT)
            ]
          }],
          maxTransfersPerBulk: [entity.maxTransfersPerBulk, {
            validators: [
              Validators.required,
              Validators.pattern(NON_NEGATIVE_INT)
            ]
          }],
          startOfDay: [entity.startOfDay, {
            validators: [
              Validators.required,
              Validators.pattern(TIME_24)
            ]
          }],
          endOfDay: [entity.endOfDay, {
            validators: [
              Validators.required,
              Validators.pattern(TIME_24)
            ]
          }],
          mailboxPathIn: [entity.mailboxPathIn, Validators.required],
          mailboxPathOut: [entity.mailboxPathOut, Validators.required],
          mqQueueIn: [entity.mqQueueIn],
          mqQueueOut: [entity.mqQueueOut],
          compression: [entity.compression],
          entityParticipantType: [entity.entityParticipantType],
          directParticipant: [entity.directParticipant]
        }, { validators: this.entityValidators.directParticipantValidator() });
        this.schedulesFormGroup = this.formBuilder.group({
          schedules: [entity.schedules || []]
        });
        this.updateSchedulesDataSource();
        this.entityService.getScheduleFileTypes().pipe(data => this.setLoading(data)).subscribe((data: string[]) => {
          this.isLoading = false;
          this.scheduleFileTypes = data;
        }, error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
        this.mqDetailsFormGroup = this.formBuilder.group({
          mqHost: [entity.mqHost],
          mqPort: [entity.mqPort, Validators.pattern(NON_NEGATIVE_INT)],
          mqQManager: [entity.mqQManager],
          mqChannel: [entity.mqChannel],
          mqQueueName: [entity.mqQueueName],
          mqQueueBinding: [entity.mqQueueBinding],
          mqQueueContext: [entity.mqQueueContext],
          mqDebug: [entity.mqDebug],
          mqSSLOptions: [entity.mqSSLOptions],
          mqSSLCiphers: [entity.mqSSLCiphers],
          mqSSLKeyCert: [entity.mqSSLKeyCert],
          mqSSLCaCert: [entity.mqSSLCaCert],
          mqHeader: [entity.mqHeader],
          mqSessionTimeout: [entity.mqSessionTimeout, Validators.pattern(NON_NEGATIVE_INT)]
        });
        this.entityService.getMQDetails().pipe(data => this.setLoading(data)).subscribe((data: MQDetails) => {
          this.isLoading = false;
          this.mqDetails = data;
        }, error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
        this.resetSwiftValidators(value);
        this.resetMqValidators(this.entityPageFormGroup.controls.entityParticipantType.value);
        this.entityPageFormGroup.controls.entityParticipantType.valueChanges.subscribe((value) => {
          this.resetMqValidators(value);
        });

        break;
      case ENTITY_SERVICE_TYPE.GPL:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [{ value: entity.entity, disabled: this.isEditing() }, {
            validators: [
              Validators.required,
              this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
            ],
            asyncValidators: !this.isEditing() && this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
            updateOn: 'blur'
          }],
          routeInbound: [entity.routeInbound, Validators.required],
          inboundRequestorDN: [entity.inboundRequestorDN, {
            validators: [
              Validators.pattern(SWIFT_DN)
            ]
          }],
          inboundResponderDN: [entity.inboundResponderDN, {
            validators: [
              Validators.pattern(SWIFT_DN)
            ]
          }],
          inboundService: [entity.inboundService, Validators.required],
          inboundRequestType: [entity.inboundRequestType],
          inboundDir: [entity.inboundDir, Validators.required],
          inboundRoutingRule: [entity.inboundRoutingRule, Validators.required]
        });
        this.entityService.getInboundRequestTypes().pipe(data => this.setLoading(data)).subscribe((data: string[]) => {
          this.isLoading = false;
          this.inboundRequestTypeList = data;
        }, error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
        this.schedulesFormGroup = null;
        this.mqDetailsFormGroup = null;
        this.resetSwiftValidators(value);

        this.onRouteInboundChanging(this.entityPageFormGroup.controls.routeInbound.value);
        this.entityPageFormGroup.controls.routeInbound.valueChanges
          .subscribe((value: boolean) => this.onRouteInboundChanging(value));
        break;
    }
  }

  resetMqValidators(value) {
    const port = this.mqDetailsFormGroup.controls.mqPort;
    const sessionTimeout = this.mqDetailsFormGroup.controls.mqSessionTimeout;
    const requestType = this.SWIFTDetailsFormGroup.controls.requestType;
    const serviceName = this.SWIFTDetailsFormGroup.controls.serviceName;
    if (value === 'DIRECT') {
      for (const control in this.mqDetailsFormGroup.controls) {
        if (this.mqDetailsFormGroup.contains(control)) {
          this.mqDetailsFormGroup.get(control).setValidators([Validators.required]);
          this.requiredFields[control] = true;
        }
      }
      requestType.setValidators(Validators.required);
      serviceName.setValidators(Validators.required);
      this.requiredFields = {
        ...this.requiredFields,
        serviceName: true,
        requestType: true
      };
    } else {
      for (const control in this.mqDetailsFormGroup.controls) {
        if (this.mqDetailsFormGroup.contains(control)) {
          this.mqDetailsFormGroup.get(control).clearValidators();
          this.requiredFields[control] = false;
        }
      }
      requestType.clearValidators();
      serviceName.clearValidators();
      this.requiredFields = {
        ...this.requiredFields,
        serviceName: false,
        requestType: false
      };
    }
    port.setValidators(
      port.validator == null ?
        Validators.pattern(NON_NEGATIVE_INT) :
        [port.validator, Validators.pattern(NON_NEGATIVE_INT)]
    );
    sessionTimeout.setValidators(
      sessionTimeout.validator == null ?
        Validators.pattern(NON_NEGATIVE_INT) :
        [sessionTimeout.validator, Validators.pattern(NON_NEGATIVE_INT)]
    );
  }

  resetSwiftValidators(value) {
    const reqDn = this.SWIFTDetailsFormGroup.controls.requestorDN;
    const resDn = this.SWIFTDetailsFormGroup.controls.responderDN;
    const serviceName = this.SWIFTDetailsFormGroup.controls.serviceName;
    if (value === ENTITY_SERVICE_TYPE.GPL) {
      reqDn.setValidators(Validators.required);
      resDn.setValidators(Validators.required);
      serviceName.setValidators(Validators.required);
      this.requiredFields = {
        ...this.requiredFields,
        requestorDN: true,
        responderDN: true,
        serviceName: true,
      };
    }
    else {
      reqDn.clearValidators();
      resDn.clearValidators();
      serviceName.clearValidators();
      this.requiredFields = {
        ...this.requiredFields,
        requestorDN: false,
        responderDN: false,
        serviceName: false,
      };
    }

    reqDn.setValidators(
      reqDn.validator == null ?
        Validators.pattern(SWIFT_DN) :
        [reqDn.validator, Validators.pattern(SWIFT_DN)]
    );

    resDn.setValidators(
      resDn.validator == null ?
        Validators.pattern(SWIFT_DN) :
        [resDn.validator, Validators.pattern(SWIFT_DN)]
    );
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

  onRouteInboundChanging = (value: boolean) => {
    if (!value) {
      this.entityPageFormGroup.controls.inboundRequestorDN.disable();
      this.entityPageFormGroup.controls.inboundRequestorDN.reset();
      this.entityPageFormGroup.controls.inboundResponderDN.disable();
      this.entityPageFormGroup.controls.inboundResponderDN.reset();
      this.entityPageFormGroup.controls.inboundService.disable();
      this.entityPageFormGroup.controls.inboundRequestType.clearValidators();
      this.requiredFields = {
        ...this.requiredFields,
        inboundRequestType: false,
      };
      this.entityPageFormGroup.controls.inboundDir.disable();
      this.entityPageFormGroup.controls.inboundDir.setValue(false);
      this.entityPageFormGroup.controls.inboundRoutingRule.disable();
      this.entityPageFormGroup.controls.inboundRoutingRule.setValue(false);
    }
    else {
      this.entityPageFormGroup.controls.inboundRequestorDN.enable();
      this.entityPageFormGroup.controls.inboundResponderDN.enable();
      this.entityPageFormGroup.controls.inboundService.enable();
      this.entityPageFormGroup.controls.inboundRequestType.setValidators(Validators.required);
      this.requiredFields = {
        ...this.requiredFields,
        inboundRequestType: true,
      };
      this.entityPageFormGroup.controls.inboundDir.enable();
      this.entityPageFormGroup.controls.inboundDir.setValue(true);

      this.entityPageFormGroup.controls.inboundRoutingRule.enable();
      this.entityPageFormGroup.controls.inboundRoutingRule.setValue(true);
    }
  }

  sendEntity(isEditing: boolean) {
    const entityName = this.entityPageFormGroup.get('entity').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `${isEditing ? 'Edit' : 'Create'} ${entityName} entity`,
      text: `Are you sure to ${isEditing ? 'edit' : 'create'} ${entityName} entity?`,
      yesCaption: isEditing ? 'Edit' : 'Create',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      this.errorMessage = null;
      if (result) {
        const entity = {
          ...this.entityTypeFormGroup.value,
          ...this.entityPageFormGroup.value,
          ...this.SWIFTDetailsFormGroup.value,
          ...this.summaryPageFormGroup.value,
          ...this.schedulesFormGroup && this.schedulesFormGroup.value,
          ...this.mqDetailsFormGroup && this.mqDetailsFormGroup.value,
        };
        let entityAction: Observable<Entity>;
        const edi = this.editableEntity;
        if (isEditing) {
          const editableEntity = this.editableEntity;
          entityAction = this.entityService.editEntity(removeEmpties({ ...editableEntity, ...entity }));
        }
        else {
          entityAction = this.entityService.createEntity(removeEmpties(entity));
        }
        entityAction.pipe(data => this.setLoading(data)).subscribe(
          () => {
            this.isLoading = false;
            this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
              title: `Entity ${isEditing ? 'edited' : 'created'}`,
              text: `Entity ${entityName} has been ${isEditing ? 'edited' : 'created'}`,
              shouldHideYesCaption: true,
              noCaption: 'Back'
            })).afterClosed().subscribe(() => {
              if (isEditing) {
                this.router.navigate(['/' + ROUTING_PATHS.ENTITIES + '/' + ROUTING_PATHS.SEARCH], { state: window.history.state });
              }
              else {
                this.stepper.reset();
                this.resetAllForms();
              }
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
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

  cancelEntity() {
    const entity = this.entityPageFormGroup.get('entity');
    const entityName = entity ? entity.value : 'new';
    const dialogRef: MatDialogRef<ConfirmDialogComponent, boolean> = this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Cancel ${this.isEditing() ? 'editing' : 'creation'} of the ${entityName} entity`,
      text: `Are you sure to cancel the ${this.isEditing() ? 'editing' : 'creation'} of the ${entityName} entity?`,
      yesCaption: `Cancel ${this.isEditing() ? 'editing' : 'creation'}`,
      yesCaptionColor: 'warn',
      noCaption: 'Back'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (this.isEditing()) {
          this.router.navigate(['/' + ROUTING_PATHS.ENTITIES + '/' + ROUTING_PATHS.SEARCH], { state: window.history.state });
        }
        else {
          this.router.navigate(['/' + ROUTING_PATHS.ENTITIES]);
        }
      }
    });
  }

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);

  getSummaryFieldsSource() {
    const entity = {
      ...this.entityTypeFormGroup.value,
      ...this.entityPageFormGroup.value,
      ...this.getSchedulesForSummaryPage(this.schedulesFormGroup && this.schedulesFormGroup.get('schedules').value),
      ...this.mqDetailsFormGroup && this.mqDetailsFormGroup.value,
      ...this.SWIFTDetailsFormGroup.value,
      ...this.summaryPageFormGroup.value
    };
    this.summaryPageDataSource = Object.keys(entity)
      .map((key) => ({
        field: key,
        value: entity[key],
        error: getErrorByField(key, this.errorMessage)
      })).filter(el => el.field !== 'changerComments');
  }

  getSchedulesForSummaryPage = (schedules: Schedule[]) => schedules &&
  {
    windowSchedules: schedules.filter((el: Schedule) => el.isWindow).map((el: Schedule) => `${el.timeStart}-${el.windowEnd}(${el.windowInterval})`).join(', '),
    dailySchedules: schedules.filter((el: Schedule) => !el.isWindow).map((el: Schedule) => el.timeStart).join(', ')
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

  isEditing = (): boolean => !isEmpty(this.editableEntity);

  getEntityServicesArray = () => Object.keys(ENTITY_SERVICE_TYPE).map(e => ENTITY_SERVICE_TYPE[e]);

  updateSchedulesDataSource = () =>
    this.schedulesDataSource = new MatTableDataSource(this.schedulesFormGroup.get('schedules').value)

  getFormattedSchedule = (schedule: Schedule) =>
    `${schedule.timeStart}${schedule.isWindow ? ' to ' + schedule.windowEnd +
      (schedule.windowInterval > 0 ? ' (every ' + schedule.windowInterval + ' minutes)' : '') : ''}`

  openScheduleDialog = (scheduleRow?: { schedule: Schedule, index: number }) =>
    this.dialog.open(EntityScheduleDialogComponent, new EntityScheduleDialogConfig({
      title: `${this.entityPageFormGroup.get('entity').value}: Schedules ${get(scheduleRow, 'schedule') ? 'Edit' : 'Add'}`,
      actionData: { editSchedule: get(scheduleRow, 'schedule'), fileTypes: this.scheduleFileTypes }
    })).afterClosed().subscribe(data => {
      if (get(data, 'editedSchedule')) {
        this.schedulesFormGroup.get('schedules').value[scheduleRow.index] = data.editedSchedule;
        this.updateSchedulesDataSource();
      }
      if (get(data, 'newSchedule')) {
        this.schedulesFormGroup.get('schedules').value.push(data.newSchedule);
        this.updateSchedulesDataSource();
      }
    })

  deleteSchedule = (schedule: Schedule, index: number) => this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
    title: `Deleting ${this.getFormattedSchedule(schedule)} schedule`,
    text: `Are you sure to delete ${this.getFormattedSchedule(schedule)} schedule?`,
    yesCaption: `Delete`,
    yesCaptionColor: 'warn',
    noCaption: 'Back'
  })).afterClosed().subscribe(result => {
    if (result) {
      this.schedulesFormGroup.get('schedules').value.splice(index, 1);
      this.updateSchedulesDataSource();
    }
  })

  getTooltip(field: string, step: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'entity',
      qualifier: (this.selectedService.toLowerCase() || 'sct') + '-' + step,
      mode: this.isEditing() ? 'edit' : 'create',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : (this.entityDisplayNames[field] || '');
  }

}
