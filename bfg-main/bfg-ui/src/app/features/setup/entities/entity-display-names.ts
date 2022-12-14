import { Entity } from 'src/app/shared/models/entity/entity.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { get, isEqual } from 'lodash';
import { difference } from 'src/app/shared/utils/utils';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-constants';
import { Schedule } from 'src/app/shared/models/schedule/schedule.model';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { formatDate } from '@angular/common';

export const ENTITY_DISPLAY_NAMES = {
  entityId: 'Entity ID',
  service: 'Service',
  entity: 'Entity',
  routeInbound: 'Route Inbound',
  inboundRequestorDN: 'Inbound Requestor DN',
  inboundResponderDN: 'Inbound Responder DN',
  inboundService: 'Inbound Service',
  inboundRequestType: 'Inbound Request Type',
  inboundDir: 'Inbound Directory',
  inboundRoutingRule: 'Inbound Routing Rule',
  requestorDN: 'Requestor DN',
  responderDN: 'Responder DN',
  requestType: 'Request Type',
  trace: 'Trace',
  snF: 'SnF',
  deliveryNotification: 'Delivery Notification',
  nonRepudiation: 'Non-Repudiation',
  e2eSigning: 'End to End Signing',
  deliveryNotifDN: 'Delivery Notification DN',
  deliveryNotifRT: 'Delivery Notification RT',
  requestRef: 'Request Reference',
  fileInfo: 'File Info',
  fileDesc: 'File Description',
  transferInfo: 'Transfer Info',
  transferDesc: 'Transfer Description',
  changerComments: 'Changer comments',
  changeID: 'Changer ID',
  serviceName: 'Service',
  isWindow: 'Type',
  timeStart: 'Time Start',
  windowEnd: 'Time End',
  windowInterval: 'Time Interval',
  fileType: 'FileType',
  lastRun: 'Last Run',
  nextRun: 'Next Run',
  windowSchedules: 'Window Schedules',
  dailySchedules: 'Daily Schedules',
  maxBulksPerFile: 'Max bulks per file',
  maxTransfersPerBulk: 'Max transactions per bulk',
  startOfDay: 'Start of Day',
  endOfDay: 'End of Day',
  mailboxPathIn: 'Inbound Mailbox',
  mailboxPathOut: 'Outbound Mailbox',
  mqQueueIn: 'Inbound MQ Queue',
  mqQueueOut: 'Outbound MQ Queue',
  compression: 'Outbound Compression',
  entityParticipantType: 'Entity Participant Type',
  directParticipant: 'Direct Participant',
  mqHost: 'MQ Host',
  mqPort: 'MQ Port',
  mqQManager: 'MQ Queue Manager',
  mqChannel: 'MQ Channel',
  mqQueueName: 'MQ Queue Name',
  mqQueueBinding: 'MQ Queue Binding',
  mqQueueContext: 'MQ Queue Context',
  mqDebug: 'MQ Debug',
  mqSSLOptions: 'MQ SSL Options',
  mqSSLCiphers: 'MQ SSL Ciphers',
  mqSSLKeyCert: 'MQ SSL Key Certificate',
  mqSSLCaCert: 'MQ SSL CA Certificate',
  mqHeader: 'MQ Header',
  mqSessionTimeout: 'MQ Session Timeout',
};

export const getEntityDisplayName = (key: string) => ENTITY_DISPLAY_NAMES[key] || key;

const getEntityDetailsSectionItems = (entity, targetService?) => ({
  'Entity Details': [
    { fieldName: 'entityId', fieldValue: entity.entityId },
    { fieldName: 'entity', fieldValue: entity.entity },
    { fieldName: 'service', fieldValue: entity.service, shouldDisplayValueUpperCase: true },
    ...(entity.service === ENTITY_SERVICE_TYPE.SCT || targetService === ENTITY_SERVICE_TYPE.SCT)
    && [{ fieldName: 'maxBulksPerFile', fieldValue: entity.maxBulksPerFile },
    { fieldName: 'maxTransfersPerBulk', fieldValue: entity.maxTransfersPerBulk },
    { fieldName: 'compression', fieldValue: entity.compression },
    { fieldName: 'startOfDay', fieldValue: entity.startOfDay },
    { fieldName: 'endOfDay', fieldValue: entity.endOfDay },
    { fieldName: 'entityParticipantType', fieldValue: entity.entityParticipantType },
    { fieldName: 'directParticipant', fieldValue: entity.directParticipant }],
  ],
  'Schedules': [{
    fieldName: 'Schedules', fieldValue: entity.schedules && entity.schedules.map((schedule: Schedule) => getScheduleRowFormat(schedule))
  }],
  'MQ Details': [
    { fieldName: 'mqHost', fieldValue: entity.mqHost },
    { fieldName: 'mqPort', fieldValue: entity.mqPort },
    { fieldName: 'mqQManager', fieldValue: entity.mqQManager },
    { fieldName: 'mqChannel', fieldValue: entity.mqChannel },
    { fieldName: 'mqQueueName', fieldValue: entity.mqQueueName },
    { fieldName: 'mqQueueBinding', fieldValue: entity.mqQueueBinding },
    { fieldName: 'mqQueueContext', fieldValue: entity.mqQueueContext },
    { fieldName: 'mqDebug', fieldValue: entity.mqDebug },
    { fieldName: 'mqSSLOptions', fieldValue: entity.mqSSLOptions },
    { fieldName: 'mqSSLCiphers', fieldValue: entity.mqSSLCiphers },
    { fieldName: 'mqSSLKeyCert', fieldValue: entity.mqSSLKeyCert },
    { fieldName: 'mqSSLCaCert', fieldValue: entity.mqSSLCaCert },
    { fieldName: 'mqHeader', fieldValue: entity.mqHeader },
    { fieldName: 'mqSessionTimeout', fieldValue: entity.mqSessionTimeout },
  ],
  'SWIFT Details': [
    { fieldName: 'requestorDN', fieldValue: entity.requestorDN },
    { fieldName: 'responderDN', fieldValue: entity.responderDN },
    { fieldName: 'serviceName', fieldValue: entity.serviceName },
    { fieldName: 'requestType', fieldValue: entity.requestType },
    { fieldName: 'snF', fieldValue: entity.snF },
    { fieldName: 'trace', fieldValue: entity.trace },
    { fieldName: 'deliveryNotification', fieldValue: entity.deliveryNotification },
    ...(entity.service === ENTITY_SERVICE_TYPE.GPL) && [
      { fieldName: 'nonRepudiation', fieldValue: entity.nonRepudiation },
      { fieldName: 'e2eSigning', fieldValue: entity.e2eSigning },
    ],
    { fieldName: 'deliveryNotifDN', fieldValue: entity.deliveryNotifDN },
    { fieldName: 'deliveryNotifRT', fieldValue: entity.deliveryNotifRT },
    { fieldName: 'requestRef', fieldValue: entity.requestRef },
    { fieldName: 'fileDesc', fieldValue: entity.fileDesc },
    { fieldName: 'fileInfo', fieldValue: entity.fileInfo },
    { fieldName: 'transferInfo', fieldValue: entity.transferInfo },
    { fieldName: 'transferDesc', fieldValue: entity.transferDesc },
    ...(entity.service === ENTITY_SERVICE_TYPE.SCT || targetService === ENTITY_SERVICE_TYPE.SCT)
    && [{ fieldName: 'mailboxPathIn', fieldValue: entity.mailboxPathIn },
    { fieldName: 'mailboxPathOut', fieldValue: entity.mailboxPathOut },
    { fieldName: 'mqQueueIn', fieldValue: entity.mqQueueIn },
    { fieldName: 'mqQueueOut', fieldValue: entity.mqQueueOut }]
  ],
  'Routing Details': [
    { fieldName: 'inboundRequestorDN', fieldValue: entity.inboundRequestorDN },
    { fieldName: 'inboundResponderDN', fieldValue: entity.inboundResponderDN },
    { fieldName: 'inboundService', fieldValue: entity.inboundService },
    { fieldName: 'inboundRequestType', fieldValue: entity.inboundRequestType },
    ...(get(entity, 'operation') === CHANGE_OPERATION.DELETE)
    && [
      { fieldName: 'inboundDir', fieldValue: entity.routeInbound ? 'Yes' : 'No' },
      { fieldName: 'inboundRoutingRule', fieldValue: entity.routeInbound ? 'Yes' : 'No' }
    ]
  ]
});

export const getEntityDetailsTabs = (entity: Entity, service: string = null): Tab[] => {
  const serviceType = service || entity.service;

  return [
    {
      tabTitle: 'Entity Details',
      tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['Entity Details'] }]
    },
    serviceType === ENTITY_SERVICE_TYPE.SCT && {
      tabTitle: 'MQ Details',
      tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['MQ Details'] }]
    },
    serviceType === ENTITY_SERVICE_TYPE.SCT && {
      tabTitle: 'Schedules',
      tabSections: [],
      tableObject: {
        tableColumns: ['isWindow', 'timeStart', 'windowEnd', 'windowInterval', 'fileType', 'lastRun', 'nextRun'],
        tableDataSource: entity.schedules.map((schedule: Schedule) =>
        ({
          ...schedule,
          isWindow: schedule.isWindow ? SCHEDULE_TYPE.WINDOW : SCHEDULE_TYPE.DAILY
        }))
      }
    },
    {
      tabTitle: 'SWIFT Details',
      tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['SWIFT Details'] }]
    },
    serviceType === ENTITY_SERVICE_TYPE.GPL && {
      tabTitle: `${serviceType} Routing Details`,
      tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['Routing Details'] }]
    }
  ].filter(el => el);
};

export const getPendingChangesTabs = (changeControl: ChangeControl, isApprovingAction?: boolean): Tab[] => [
  {
    tabTitle: DIALOG_TABS.CHANGE_DETAILS,
    tabSections: [{
      sectionItems: [
        { fieldName: 'Change ID', fieldValue: changeControl.changeID },
        { fieldName: 'Object type', fieldValue: changeControl.objectType },
        { fieldName: 'Operation', fieldValue: changeControl.operation },
        { fieldName: 'Status', fieldValue: changeControl.status },
        { fieldName: 'Changer', fieldValue: changeControl.changer },
        { fieldName: 'Date Changed', fieldValue: formatDate(changeControl.dateChanged, 'yyyy-MM-dd HH:mm:ss', 'en-GB') },
        { fieldName: 'Changer Notes', fieldValue: changeControl.changerComments },
        !isApprovingAction && { fieldName: 'Approver Notes', fieldValue: changeControl.approverComments },
      ],
    }]
  },
  changeControl.entityBefore && {
    tabTitle: DIALOG_TABS.BEFORE_CHANGES,
    tabSections: [
      { sectionTitle: 'Entity Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['Entity Details'] },
      changeControl.entityBefore.service === ENTITY_SERVICE_TYPE.SCT &&
      { sectionTitle: 'MQ Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['MQ Details'] },
      { sectionTitle: 'SWIFT Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['SWIFT Details'] },
      changeControl.entityBefore.service === ENTITY_SERVICE_TYPE.GPL && {
        sectionTitle: `${changeControl.entityBefore.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['Routing Details']
      }
    ],
    tableObject: changeControl.entityBefore.service === ENTITY_SERVICE_TYPE.SCT && {
      tableColumns: ['isWindow', 'timeStart', 'windowEnd', 'windowInterval', 'fileType', 'lastRun', 'nextRun'],
      tableDataSource: changeControl.entityBefore.schedules.map((schedule: Schedule) =>
      ({
        ...schedule,
        isWindow: schedule.isWindow ? SCHEDULE_TYPE.WINDOW : SCHEDULE_TYPE.DAILY
      })),
      tableTitle: 'Schedules',
      formatRow: getScheduleRowFormat
    }
  },
  changeControl.operation !== CHANGE_OPERATION.DELETE &&
  {
    tabTitle: DIALOG_TABS.AFTER_CHANGES,
    tabSections: [
      { sectionTitle: 'Entity Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['Entity Details'] },
      changeControl.entityLog.service === ENTITY_SERVICE_TYPE.SCT &&
      { sectionTitle: 'MQ Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['MQ Details'] },
      { sectionTitle: 'SWIFT Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['SWIFT Details'] },
      changeControl.entityLog.service === ENTITY_SERVICE_TYPE.GPL && {
        sectionTitle: `${changeControl.entityLog.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['Routing Details']
      }
    ],
    tableObject: changeControl.entityLog.service === ENTITY_SERVICE_TYPE.SCT && {
      tableColumns: ['isWindow', 'timeStart', 'windowEnd', 'windowInterval', 'fileType', 'lastRun', 'nextRun'],
      tableDataSource: changeControl.entityLog.schedules.map((schedule: Schedule) =>
      ({
        ...schedule,
        isWindow: schedule.isWindow ? SCHEDULE_TYPE.WINDOW : SCHEDULE_TYPE.DAILY
      })),
      tableTitle: 'Schedules'
    }
  },
  changeControl.entityBefore && changeControl.operation !== CHANGE_OPERATION.DELETE &&
  {
    tabTitle: DIALOG_TABS.DIFFERENCES,
    tabSections: [
      {
        sectionTitle: 'Entity Details',
        sectionItems: getEntityDetailsSectionItems(
          difference(changeControl.entityLog, changeControl.entityBefore), changeControl.entityLog.service)['Entity Details']
      },
      changeControl.entityLog.service === ENTITY_SERVICE_TYPE.SCT &&
      {
        sectionTitle: 'MQ Details',
        sectionItems: getEntityDetailsSectionItems(difference(changeControl.entityLog, changeControl.entityBefore))['MQ Details']
      },
      changeControl.entityLog.service === ENTITY_SERVICE_TYPE.SCT &&
      {
        sectionTitle: 'Schedules',
        sectionItems:
          getEntityDetailsSectionItems(isSchedulesDifferent(changeControl.entityBefore.schedules, changeControl.entityLog.schedules)
            && changeControl.entityLog)['Schedules']
      },
      {
        sectionTitle: 'SWIFT Details',
        sectionItems: getEntityDetailsSectionItems(
          difference(changeControl.entityLog, changeControl.entityBefore), changeControl.entityLog.service)['SWIFT Details']
      },
      changeControl.entityLog.service === ENTITY_SERVICE_TYPE.GPL &&
      {
        sectionTitle: `${changeControl.entityBefore.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(difference(changeControl.entityLog, changeControl.entityBefore))['Routing Details']
      }
    ]
  }
].filter(el => el);

const getScheduleRowFormat = (schedule) => {
  const windowType = typeof schedule.isWindow === 'string' ?
    SCHEDULE_TYPE[schedule.isWindow.toUpperCase()] : (schedule.isWindow ? SCHEDULE_TYPE.WINDOW : SCHEDULE_TYPE.DAILY);
  return `${windowType} ${schedule.timeStart}${windowType === SCHEDULE_TYPE.WINDOW
    ? `-${schedule.windowEnd}(${schedule.windowInterval})` : ''} ${schedule.fileType} (${schedule.lastRun
      ? `Last Run: ${schedule.lastRun}, ` : ''}${schedule.nextRun ? `Next Run: ${schedule.nextRun}` : ''})`;
};

const isSchedulesDifferent = (schedulesBefore, schedulesAfter) =>
  !isEqual(schedulesBefore.map((el) => getScheduleRowFormat(el)), schedulesAfter.map((el) => getScheduleRowFormat(el)));

