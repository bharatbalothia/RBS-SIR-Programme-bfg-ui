import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ENTITY_VALIDATION_MESSAGES } from '../validation-messages';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { get, isEmpty } from 'lodash';
import { DISPLAY_NAMES } from '../display-names';
import { EntityValidators } from '../../../../shared/models/entity/entity-validators';
import { SWIFT_DN } from 'src/app/core/constants/validation-regexes';
import { ActivatedRoute, Router } from '@angular/router';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { Observable, scheduled } from 'rxjs';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-service-type';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { Schedule } from 'src/app/shared/models/schedule/schedule.model';
import { EntityScheduleDialogComponent } from '../entity-schedule-dialog/entity-schedule-dialog.component';
import { EntityScheduleDialogConfig } from '../entity-schedule-dialog/entity-schedule-dialog-config.model';
import { MatTableDataSource } from '@angular/material/table';
import * as moment from 'moment';
import { MQDetails } from 'src/app/shared/models/entity/mq-details.model';

@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  entityDisplayNames = DISPLAY_NAMES;
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

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private entityService: EntityService,
    private entityValidators: EntityValidators,
    private activatedRouter: ActivatedRoute,
    private router: Router,
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
          Validators.required,
          Validators.pattern(SWIFT_DN)
        ]
      }],
      responderDN: [entity.responderDN, {
        validators: [
          Validators.required,
          Validators.pattern(SWIFT_DN)
        ]
      }],
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
    this.formGroups.forEach(formGroup => !formGroup.control.get('service') && formGroup.resetForm());
    this.initializeFormGroups({ ...entity, service: value });
    switch (value) {
      case ENTITY_SERVICE_TYPE.SCT:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [entity.entity, {
            validators: [
              Validators.required,
              this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
            ],
            asyncValidators: !this.isEditing() && this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
            updateOn: 'blur'
          }],
          maxBulksPerFile: [entity.maxBulksPerFile, Validators.required],
          maxTransfersPerBulk: [entity.maxTransfersPerBulk, Validators.required],
          startOfDay: [entity.startOfDay, Validators.required],
          endOfDay: [entity.endOfDay, Validators.required],
          mailboxPathIn: [entity.mailboxPathIn, Validators.required],
          mailboxPathOut: [entity.mailboxPathOut, Validators.required],
          mqQueueIn: [entity.mqQueueIn],
          mqQueueOut: [entity.mqQueueOut],
          compression: [entity.compression],
          entityParticipantType: [entity.entityParticipantType],
          directParticipant: [entity.directParticipant]
        }, {validators: this.entityValidators.directParticipantValidator()});
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
          mqPort: [entity.mqPort],
          mqQManager: [entity.mqQManager],
          mqChannel: [entity.mqChannel],
          mqQueueName: [entity.mqQueueName],
          mqQueueBinding: [entity.mqQueueBinding],
          mqQueueContext: [entity.mqQueueContext],
          mqDebug: [entity.mqDebug],
          mqSSLoptions: [entity.mqSSLoptions],
          mqSSLciphers: [entity.mqSSLciphers],
          mqSSLkey: [entity.mqSSLkey],
          mqSSLcaCert: [entity.mqSSLcaCert],
          mqHeader: [entity.mqHeader],
          mqSessionTimeout: [entity.mqSessionTimeout]
        });
        this.entityService.getMQDetails().pipe(data => this.setLoading(data)).subscribe((data: MQDetails) => {
          this.isLoading = false;
          this.mqDetails = data;
        }, error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
        break;
      case ENTITY_SERVICE_TYPE.GPL:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [entity.entity, {
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
              Validators.required,
              Validators.pattern(SWIFT_DN)
            ]
          }],
          inboundResponderDN: [entity.inboundResponderDN, {
            validators: [
              Validators.required,
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
        break;
    }
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

  sendEntity(isEditing: boolean) {
    const entityName = this.entityTypeFormGroup.get('service').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `${isEditing ? 'Edit' : 'Create'} ${entityName} entity`,
      text: `Are you sure to ${isEditing ? 'edit' : 'create'} ${entityName} entity?`,
      yesCaption: isEditing ? 'Edit' : 'Create',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      this.errorMessage = null;
      if (result) {
        const entity = removeEmpties({
          ...this.entityTypeFormGroup.value,
          ...this.entityPageFormGroup.value,
          ...this.SWIFTDetailsFormGroup.value,
          ...this.summaryPageFormGroup.value,
          ...this.schedulesFormGroup && this.convertScheduleListToUnix(this.schedulesFormGroup.get('schedules').value),
        });
        let entityAction: Observable<Entity>;
        const edi = this.editableEntity;
        if (isEditing) {
          const editableEntity = this.editableEntity;
          entityAction = this.entityService.editEntity({ ...editableEntity, ...entity });
        }
        else {
          entityAction = this.entityService.createEntity(entity);
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
    this.isLoading = true;
    return data;
  }

  cancelEntity() {
    const entityName = this.entityTypeFormGroup.get('service').value || 'new';
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
          this.errorMessage = null;
          this.stepper.reset();
          this.resetAllForms();
        }
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
      ...this.getSchedulesForSummaryPage(this.schedulesFormGroup && this.schedulesFormGroup.get('schedules').value),
      ...this.mqDetailsFormGroup && this.mqDetailsFormGroup.value,
      ...this.SWIFTDetailsFormGroup.value,
      ...this.summaryPageFormGroup.value
    });
    this.summaryPageDataSource = Object.keys(entity)
      .map((key) => ({
        field: key,
        value: entity[key],
        error: this.getErrorByField(key)
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
    this.schedulesDataSource = new MatTableDataSource(this.schedulesFormGroup.get('schedules').value);

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

  convertScheduleListToUnix = (scheduleList: Schedule[] = []) => ({
    schedules: scheduleList.map((schedule: Schedule) => ({
      ...schedule,
      timeStart: moment(schedule.timeStart, 'HH:mm').valueOf(),
      windowEnd: schedule.windowEnd && moment(schedule.windowEnd, 'HH:mm').valueOf(),
    }))
  })

  convertScheduleListToString = (scheduleList: Schedule[] = []) => ({
    schedules: scheduleList.map((schedule: Schedule) => ({
      ...schedule,
      timeStart: moment(schedule.timeStart).format('HH:mm'),
      windowEnd: schedule.windowEnd && moment(schedule.windowEnd).format('HH:mm'),
    }))
  })


}
