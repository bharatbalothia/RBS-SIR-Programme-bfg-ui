import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ENTITY_VALIDATION_MESSAGES } from '../validation-messages';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { removeNullOrUndefined } from 'src/app/shared/utils/utils';
import { ErrorMessage, getApiErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import { get, isEmpty } from 'lodash';
import { ENTITY_DISPLAY_NAMES } from '../entity-display-names';
import { EntityValidators } from '../../../../shared/models/entity/entity-validators';
import { SWIFT_DN, TIME_24, NON_NEGATIVE_INT } from 'src/app/core/constants/validation-regexes';
import { ActivatedRoute, Router } from '@angular/router';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { Observable, of } from 'rxjs';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ENTITY_SERVICE_TYPE, ENTITY_PERMISSIONS } from 'src/app/shared/models/entity/entity-constants';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { Schedule } from 'src/app/shared/models/schedule/schedule.model';
import { EntityScheduleDialogComponent } from '../entity-schedule-dialog/entity-schedule-dialog.component';
import { EntityScheduleDialogConfig } from '../entity-schedule-dialog/entity-schedule-dialog-config.model';
import { MatTableDataSource } from '@angular/material/table';
import { MQDetails } from 'src/app/shared/models/entity/mq-details.model';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { AuthService } from 'src/app/core/auth/auth.service';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { NotificationService } from 'src/app/shared/services/NotificationService';

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

  @ViewChild('stepper') stepper: MatHorizontalStepper;
  @ViewChildren(FormGroupDirective) formGroups: QueryList<FormGroupDirective>;

  inboundRequestTypeList: any[] = [];
  entityValidationMessages = ENTITY_VALIDATION_MESSAGES;
  errorMessage: ErrorMessage;

  isLoading = false;

  summaryDisplayedColumns = ['field', 'value'];
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

  pendingChange: ChangeControl;

  requiredFields: { [filedName: string]: boolean } = {};

  isCloneAction = false;

  routeInboundEntityCache: any = {};

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private entityService: EntityService,
    private entityValidators: EntityValidators,
    private activatedRouter: ActivatedRoute,
    private router: Router,
    private toolTip: TooltipService,
    private auth: AuthService,
    public notificationService: NotificationService
  ) { }

  ngOnInit() {
    this.entityTypeFormGroup = this.formBuilder.group({
      service: ['', Validators.required]
    });
    this.initializeFormGroups(this.getEntityDefaultValue());
    this.activatedRouter.params.subscribe(params => {
      if (params.entityId || params.changeId) {
        if (params.changeId) {
          this.entityService.getPendingChangeById(params.changeId)
            .pipe(data => this.setLoading(data)).subscribe((data: ChangeControl) => {
              this.isLoading = false;
              this.pendingChange = data;
              this.getEntityById(this.entityService.getPendingEntityById(this.pendingChange.changeID));
            },
              error => {
                this.isLoading = false;
                this.errorMessage = getApiErrorMessage(error);
              });
        }
        else {
          if (this.router.url.includes(ROUTING_PATHS.CLONE)) {
            this.isCloneAction = true;
          }
          this.getEntityById(this.entityService.getEntityById(params.entityId));
        }
      }
    });
  }

  getEntityById = (getEntity) =>
    getEntity.pipe(data => this.setLoading(data)).subscribe((data: Entity) => {
      this.isLoading = false;
      this.editableEntity = data;
      this.entityTypeFormGroup = this.formBuilder.group({
        service: [this.editableEntity.service, Validators.required]
      });
      if (this.isCloneAction) {
        const clonedFormEditableEntity = {
          ...this.editableEntity,
          entity: '',
          inboundRequestorDN: '',
          inboundResponderDN: '',
          inboundService: '',
          inboundRequestType: [],
          mailboxPathOut: '',
          mqQueueOut: '',
        };
        this.onServiceSelect(this.editableEntity.service.toUpperCase(), clonedFormEditableEntity);
      }
      else {
        this.onServiceSelect(this.editableEntity.service.toUpperCase(), this.editableEntity);
        this.markAllFieldsTouched();
      }
      if (this.editableEntity && !(this.isAuthorizedToProceed() && this.tryToProceedPendingEdit() && this.tryToProceedEdit())) {
        this.entityTypeFormGroup.addControl('disableProceed', new FormControl('', [Validators.required]));
      }
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      })

  initializeFormGroups(entity: Entity) {
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
    if (!this.editableEntity) {
      this.entityService.getSWIFTService().pipe(data => this.setLoading(data)).subscribe(data => {
        this.isLoading = false;
        this.SWIFTDetailsFormGroup.controls.serviceName.setValue(data);
      }, error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
    }
  }

  getEntityDefaultValue = (): Entity => ({
    service: '',
    entity: '',
    routeInbound: true,
    inboundRequestorDN: '',
    inboundResponderDN: '',
    inboundService: '',
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
    if (this.isAuthorizedToProceed()) {
      this.prepareFieldsForEntityOfType(value, entity);
    } else {
      this.entityTypeFormGroup.get('service').setErrors({ forbidden: true });
      if (this.isEditing()) {
        this.prepareFieldsForEntityOfType(value, entity);
      }
    }
  }

  prepareFieldsForEntityOfType(value, entity) {
    this.formGroups.forEach(formGroup => !formGroup.control.get('service') && formGroup.resetForm());
    this.initializeFormGroups({ ...entity, service: value });
    switch (value) {
      case ENTITY_SERVICE_TYPE.SCT:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [{ value: entity.entity, disabled: this.isEditing() && this.isChangeControlNotCreateStatus() && !this.isCloneAction }, {
            validators: [
              Validators.required,
              this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
            ],
            asyncValidators: this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
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
          mailboxPathOut: [entity.mailboxPathOut, {
            validators: [
              Validators.required
            ],
            asyncValidators: this.isNotEditOrPendingEditWithCreateStatus() && this.entityValidators.mailboxPathOutValidator(),
            updateOn: 'blur'
          }],
          mqQueueIn: [entity.mqQueueIn],
          mqQueueOut: [entity.mqQueueOut, {
            asyncValidators: this.isNotEditOrPendingEditWithCreateStatus() && this.entityValidators.mqQueueOutValidator(),
            updateOn: 'blur'
          }],
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
        this.SWIFTDetailsFormGroup.removeControl('nonRepudiation');
        this.SWIFTDetailsFormGroup.removeControl('e2eSigning');
        this.resetSwiftValidators(value);
        this.resetMqValidators(this.entityPageFormGroup.controls.entityParticipantType.value);
        this.entityPageFormGroup.controls.entityParticipantType.valueChanges.subscribe((value) => {
          this.resetMqValidators(value);
        });
        break;
      case ENTITY_SERVICE_TYPE.GPL:
        this.entityPageFormGroup = this.formBuilder.group({
          entity: [{ value: entity.entity, disabled: this.isEditing() && this.isChangeControlNotCreateStatus() && !this.isCloneAction }, {
            validators: [
              Validators.required,
              this.entityValidators.entityPatternByServiceValidator(this.entityTypeFormGroup.controls.service)
            ],
            asyncValidators: this.entityValidators.entityExistsValidator(this.entityTypeFormGroup.controls.service),
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
          inboundRequestType: [entity.inboundRequestType]
        });
        this.schedulesFormGroup = null;
        this.mqDetailsFormGroup = null;
        this.resetSwiftValidators(value);
        this.onRouteInboundChanging(this.entityPageFormGroup.controls.routeInbound.value);
        this.entityPageFormGroup.controls.routeInbound.valueChanges
          .subscribe((value: boolean) => this.onRouteInboundChanging(value));
        this.getGPLFormParams();
        break;
    }
  }

  getGPLFormParams = () => {
    this.entityService.getInboundRequestTypes().pipe(data => this.setLoading(data)).subscribe(data => {
      this.isLoading = false;
      this.inboundRequestTypeList = this.getInboundRequestTypes(data);
      if (this.isEditing()) {
        this.entityPageFormGroup.controls.inboundRequestType.setValue(this.inboundRequestTypeList
          .filter(el => (get(this.entityPageFormGroup.controls, 'inboundRequestType.value', []) as string[] || []).includes(el.value)));
      }
    }, error => {
      this.isLoading = false;
      this.errorMessage = getApiErrorMessage(error);
    });
    if (!this.editableEntity && isEmpty(this.entityPageFormGroup.controls.inboundService.value || get(this.routeInboundEntityCache, 'inboundService'))) {
      this.entityService.getInboundService().pipe(data => this.setLoading(data)).subscribe(data => {
        this.isLoading = false;
        this.entityPageFormGroup.controls.inboundService.setValue(data);
      }, error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
    }
  }

  isNotEditOrPendingEditWithCreateStatus = () => !((!this.isCloneAction && this.isEditing() && !this.pendingChange)
    || (this.pendingChange && this.pendingChange.operation !== CHANGE_OPERATION.CREATE))

  validateRouteAttributes = () => {
    if (this.entityTypeFormGroup.get('service').value === ENTITY_SERVICE_TYPE.GPL
      && this.entityPageFormGroup.valid && this.isNotEditOrPendingEditWithCreateStatus()) {
      const inboundRequestorDN = this.entityPageFormGroup.get('inboundRequestorDN').value;
      const inboundResponderDN = this.entityPageFormGroup.get('inboundResponderDN').value;
      const inboundService = this.entityPageFormGroup.get('inboundService').value;
      const inboundRequestType = this.entityPageFormGroup.get('inboundRequestType').value || [];

      this.entityService.isRouteAttributesExists({
        inboundRequestorDN,
        inboundResponderDN,
        inboundService,
        inboundRequestType: inboundRequestType.map(el => el.key).toString()
      }).pipe(data => this.setLoading(data)).subscribe((data) => {
        this.isLoading = false;
        this.stepper.next();
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
    }
    else {
      this.stepper.next();
    }
  }


  resetMqValidators(value) {
    const port = this.mqDetailsFormGroup.controls.mqPort;
    const sessionTimeout = this.mqDetailsFormGroup.controls.mqSessionTimeout;
    const requestorDN = this.SWIFTDetailsFormGroup.controls.requestorDN;
    const responderDN = this.SWIFTDetailsFormGroup.controls.responderDN;
    const requestType = this.SWIFTDetailsFormGroup.controls.requestType;
    const serviceName = this.SWIFTDetailsFormGroup.controls.serviceName;
    if (value === 'DIRECT') {
      for (const control in this.mqDetailsFormGroup.controls) {
        if (this.mqDetailsFormGroup.contains(control)) {
          this.mqDetailsFormGroup.get(control).setValidators([Validators.required]);
          this.requiredFields[control] = true;
        }
      }
      requestorDN.setValidators(Validators.required);
      responderDN.setValidators(Validators.required);
      requestType.setValidators(Validators.required);
      serviceName.setValidators(Validators.required);
      this.requiredFields = {
        ...this.requiredFields,
        requestorDN: true,
        responderDN: true,
        serviceName: true,
        requestType: true,
      };
    } else {
      for (const control in this.mqDetailsFormGroup.controls) {
        if (this.mqDetailsFormGroup.contains(control)) {
          this.mqDetailsFormGroup.get(control).clearValidators();
          this.requiredFields[control] = false;
        }
      }
      requestorDN.clearValidators();
      responderDN.clearValidators();
      requestType.clearValidators();
      serviceName.clearValidators();
      this.requiredFields = {
        ...this.requiredFields,
        requestorDN: false,
        responderDN: false,
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
    if (value === false) {
      this.routeInboundEntityCache['inboundRequestorDN'] = this.entityPageFormGroup.controls.inboundRequestorDN.value;
      this.routeInboundEntityCache['inboundResponderDN'] = this.entityPageFormGroup.controls.inboundResponderDN.value;
      this.routeInboundEntityCache['inboundService'] = this.entityPageFormGroup.controls.inboundService.value;
      this.routeInboundEntityCache['inboundRequestType'] = this.entityPageFormGroup.controls.inboundRequestType.value;
      this.entityPageFormGroup.controls.inboundRequestorDN.disable();
      this.entityPageFormGroup.controls.inboundRequestorDN.setValue(null);
      this.entityPageFormGroup.controls.inboundResponderDN.disable();
      this.entityPageFormGroup.controls.inboundResponderDN.setValue(null);
      this.entityPageFormGroup.controls.inboundService.disable();
      this.entityPageFormGroup.controls.inboundService.setValue(null);
      this.entityPageFormGroup.controls.inboundRequestType.disable();
      this.entityPageFormGroup.controls.inboundRequestType.setValue(null);
      this.entityPageFormGroup.controls.inboundRequestType.clearValidators();
      this.requiredFields = {
        ...this.requiredFields,
        inboundRequestType: false,
      };
    }
    else {
      this.entityPageFormGroup.controls.inboundRequestorDN.enable();
      this.routeInboundEntityCache.inboundRequestorDN
        && this.entityPageFormGroup.controls.inboundRequestorDN.setValue(this.routeInboundEntityCache.inboundRequestorDN);
      this.entityPageFormGroup.controls.inboundResponderDN.enable();
      this.routeInboundEntityCache.inboundResponderDN
        && this.entityPageFormGroup.controls.inboundResponderDN.setValue(this.routeInboundEntityCache.inboundResponderDN);
      this.entityPageFormGroup.controls.inboundService.enable();
      this.routeInboundEntityCache.inboundService
        && this.entityPageFormGroup.controls.inboundService.setValue(this.routeInboundEntityCache.inboundService);
      this.entityPageFormGroup.controls.inboundRequestType.enable();
      this.routeInboundEntityCache.inboundRequestType
        && this.entityPageFormGroup.controls.inboundRequestType.setValue(this.routeInboundEntityCache.inboundRequestType);
      this.entityPageFormGroup.controls.inboundRequestType.setValidators(Validators.required);
      this.requiredFields = {
        ...this.requiredFields,
        inboundRequestType: true,
      };
    }
  }

  sendEntity(isEditing: boolean) {
    const entityName = this.entityPageFormGroup.get('entity').value || 'new';
    this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
      title: `Save ${entityName} entity`,
      text: `Are you sure to save ${entityName} entity?`,
      yesCaption: 'Submit',
      noCaption: 'Cancel'
    })).afterClosed().subscribe(result => {
      this.errorMessage = null;
      if (result) {
        const schedules = get(this.schedulesFormGroup, 'controls.schedules.value');
        const entity = {
          ...this.entityTypeFormGroup.value,
          ...this.entityPageFormGroup.value,
          ...this.SWIFTDetailsFormGroup.value,
          ...this.summaryPageFormGroup.value,
          ...this.mqDetailsFormGroup && this.mqDetailsFormGroup.value,
          schedules: this.isCloneAction && schedules ? schedules.map(el => ({ ...el, scheduleId: null })) : schedules,
          inboundRequestorDN: get(this.entityPageFormGroup.controls.inboundRequestorDN, 'value'),
          inboundResponderDN: get(this.entityPageFormGroup.controls.inboundResponderDN, 'value'),
          inboundService: get(this.entityPageFormGroup.controls.inboundService, 'value'),
          inboundRequestType: (get(this.entityPageFormGroup.controls, 'inboundRequestType.value', []) as any[] || [])
            .map(el => el.key)
        };
        let entityAction: Observable<Entity>;
        if (isEditing) {
          const editableEntity = this.editableEntity;
          if (this.pendingChange) {
            entityAction =
              this.entityService.editPendingEntity(this.pendingChange.changeID, removeNullOrUndefined({ ...editableEntity, ...entity }));
          }
          else {
            entityAction = this.entityService.editEntity(removeNullOrUndefined({ ...editableEntity, ...entity }));
          }
        }
        else {
          entityAction = this.entityService.createEntity(removeNullOrUndefined(entity));
        }
        entityAction.pipe(data => this.setLoading(data)).subscribe(
          () => {
            this.isLoading = false;
            this.notificationService.show(
              'Entity saved',
              `Entity ${entityName} has been saved`,
              'success'
            );
            if (isEditing || this.isCloneAction) {
              const previousURL = get(window.history.state, 'previousURL');
              if (previousURL) {
                this.router.navigate([previousURL], { state: window.history.state });
              } else {
                this.router.navigate(['/' + ROUTING_PATHS.ENTITIES + '/' + ROUTING_PATHS.SEARCH], { state: window.history.state });
              }
            }
            else {
              this.stepper.reset();
              this.resetAllForms();
            }
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
      title: `Cancel ${!this.isCloneAction && this.isEditing() ? 'editing' : 'creation'} of the ${entityName} entity`,
      text: `Are you sure to cancel the ${!this.isCloneAction && this.isEditing() ? 'editing' : 'creation'} of the ${entityName} entity?`,
      yesCaption: `Cancel ${!this.isCloneAction && this.isEditing() ? 'editing' : 'creation'}`,
      yesCaptionColor: 'warn',
      noCaption: 'Back'
    }));
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (this.isEditing() || this.isCloneAction) {
          const previousURL = get(window.history.state, 'previousURL');
          if (previousURL) {
            this.router.navigate([previousURL], { state: window.history.state });
          } else {
            this.router.navigate(['/' + ROUTING_PATHS.ENTITIES + '/' + ROUTING_PATHS.SEARCH], { state: window.history.state });
          }
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
      ...get(this.entityTypeFormGroup.controls, 'service.value', '') === ENTITY_SERVICE_TYPE.GPL && {
        inboundRequestorDN: get(this.entityPageFormGroup.controls, 'inboundRequestorDN.value'),
        inboundResponderDN: get(this.entityPageFormGroup.controls, 'inboundResponderDN.value'),
        inboundService: get(this.entityPageFormGroup.controls, 'inboundService.value'),
        inboundRequestType: (get(this.entityPageFormGroup.controls, 'inboundRequestType.value', []) as any[] || [])
          .map(el => el.value).join(',\n'),
        routeInbound: get(this.entityPageFormGroup.value, 'routeInbound') ? 'Yes' : 'No',
        inboundDir: get(this.entityPageFormGroup.value, 'routeInbound') ? 'Yes' : 'No', // display the same value as routeInbound
        inboundRoutingRule: get(this.entityPageFormGroup.value, 'routeInbound') ? 'Yes' : 'No', // display the same value as routeInbound
      },
      ...this.getSchedulesForSummaryPage(this.schedulesFormGroup && this.schedulesFormGroup.get('schedules').value),
      ...this.mqDetailsFormGroup && this.mqDetailsFormGroup.value,
      ...this.SWIFTDetailsFormGroup.value,
      ...this.summaryPageFormGroup.value,
    };

    this.summaryPageDataSource = Object.keys(entity)
      .map((key) => ({
        field: key,
        value: entity[key],
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

  isAuthorizedToProceed() {
    const requiredPermissions = [];
    if (this.isEditing()) {
      if (this.selectedService === ENTITY_SERVICE_TYPE.SCT) {
        requiredPermissions.push(ENTITY_PERMISSIONS.EDIT_SCT);
      }
      if (this.selectedService === ENTITY_SERVICE_TYPE.GPL) {
        requiredPermissions.push(ENTITY_PERMISSIONS.EDIT_GPL);
      }
    } else {
      if (this.selectedService === ENTITY_SERVICE_TYPE.SCT) {
        requiredPermissions.push(ENTITY_PERMISSIONS.CREATE_SCT);
      }
      if (this.selectedService === ENTITY_SERVICE_TYPE.GPL) {
        requiredPermissions.push(ENTITY_PERMISSIONS.CREATE_GPL);
      }
    }
    return this.auth.isEnoughPermissions(requiredPermissions);
  }

  tryToProceed() {
    if (this.isAuthorizedToProceed() && this.tryToProceedPendingEdit() && this.tryToProceedEdit()) {
      this.stepper.next();
    } else {
      this.entityTypeFormGroup.get('service').setErrors({ forbidden: true });
      this.auth.showForbidden();
    }
  }

  tryToProceedPendingEdit = () => this.pendingChange
    ? (this.pendingChange.operation !== CHANGE_OPERATION.DELETE
      && this.auth.isTheSameUser(this.pendingChange.changer)) : true

  tryToProceedEdit = () => this.editableEntity
    ? (this.editableEntity.service === ENTITY_SERVICE_TYPE.SCT || this.editableEntity.service === ENTITY_SERVICE_TYPE.GPL) : true

  getInboundRequestTypes = (value) => value && Object.keys(value).map(el => ({ key: el, value: value[el] }));

  isChangeControlNotCreateStatus = () => get(this.pendingChange, 'operation') !== CHANGE_OPERATION.CREATE;

  shouldShowRoutingAttributesOverlay = () => this.isCloneAction && this.editableEntity && get(this.entityPageFormGroup, 'controls.routeInbound.value');

  getMaxLengthForEntityField = () => this.selectedService === ENTITY_SERVICE_TYPE.GPL ? 11 : 8;

  insert({ key, value }: { key: string, value: string | [] }) {
    switch (key) {
      case 'inboundRequestType':
        this.entityPageFormGroup.controls.inboundRequestType.setValue(
          this.inboundRequestTypeList.filter(el => value.includes(el.value as never))
        );
        break;
      default:
        this.entityPageFormGroup.controls[key].patchValue(value);
    }
  }

}
