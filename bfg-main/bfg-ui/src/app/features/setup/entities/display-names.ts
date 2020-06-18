import { Entity } from 'src/app/shared/models/entity/entity.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { isEmpty } from 'lodash';
import { difference } from 'src/app/shared/utils/utils';
import { ENTITY_APPROVING_DIALOG_TABS } from './entity-approving-dialog/entity-approving-dialog-tabs';

export const DISPLAY_NAMES = {
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
  serviceName: 'Service Name',
  isWindow: 'Type',
  timeStart: 'Time Start',
  windowEnd: 'Time End',
  windowInterval: 'Time Interval',
  fileType: 'FileType',
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

export const getDisplayName = (key: string) => DISPLAY_NAMES[key] || key;

const getEntityDetailsSectionItems = (entity) => ({
  'Entity Details': [
    { fieldName: getDisplayName('entityId'), fieldValue: entity.entityId },
    { fieldName: getDisplayName('entity'), fieldValue: entity.entity },
    { fieldName: getDisplayName('service'), fieldValue: entity.service, shouldDisplayValueUpperCase: true },
  ],
  'SWIFT Details': [
    { fieldName: getDisplayName('requestorDN'), fieldValue: entity.requestorDN },
    { fieldName: getDisplayName('responderDN'), fieldValue: entity.responderDN },
    { fieldName: getDisplayName('serviceName'), fieldValue: entity.serviceName },
    { fieldName: getDisplayName('requestType'), fieldValue: entity.requestType },
    { fieldName: getDisplayName('snF'), fieldValue: entity.snF },
    { fieldName: getDisplayName('trace'), fieldValue: entity.trace },
    { fieldName: getDisplayName('deliveryNotification'), fieldValue: entity.deliveryNotification },
    { fieldName: getDisplayName('nonRepudiation'), fieldValue: entity.nonRepudiation },
    { fieldName: getDisplayName('e2eSigning'), fieldValue: entity.e2eSigning },
    { fieldName: getDisplayName('deliveryNotifDN'), fieldValue: entity.deliveryNotifDN },
    { fieldName: getDisplayName('deliveryNotifRT'), fieldValue: entity.deliveryNotifRT },
    { fieldName: getDisplayName('requestRef'), fieldValue: entity.requestRef },
    { fieldName: getDisplayName('fileDesc'), fieldValue: entity.fileDesc },
    { fieldName: getDisplayName('fileInfo'), fieldValue: entity.fileInfo },
    { fieldName: getDisplayName('transferInfo'), fieldValue: entity.transferInfo },
    { fieldName: getDisplayName('transferDesc'), fieldValue: entity.transferDesc },
  ],
  'Routing Details': [
    { fieldName: getDisplayName('inboundRequestorDN'), fieldValue: entity.inboundRequestorDN },
    { fieldName: getDisplayName('inboundResponderDN'), fieldValue: entity.inboundResponderDN },
    { fieldName: getDisplayName('inboundService'), fieldValue: entity.inboundService },
    { fieldName: getDisplayName('inboundRequestType'), fieldValue: entity.inboundRequestType },
  ]
});

export const getEntityDetailsFields = (entity: Entity): Tab[] => [
  {
    tabTitle: 'Entity Details',
    tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['Entity Details'] }]
  },
  {
    tabTitle: 'SWIFT Details',
    tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['SWIFT Details'] }]
  },
  {
    tabTitle: `${entity.service} Routing Details`,
    tabSections: [{ sectionItems: getEntityDetailsSectionItems(entity)['Routing Details'] }]
  }
];

export const getPendingChangesFields = (changeControl: ChangeControl): Tab[] => [
  {
    tabTitle: ENTITY_APPROVING_DIALOG_TABS.CHANGE_DETAILS,
    tabSections: [{
      sectionItems: [
        { fieldName: 'Change ID', fieldValue: changeControl.changeID },
        { fieldName: 'Object type', fieldValue: changeControl.objectType },
        { fieldName: 'Operation', fieldValue: changeControl.operation },
        { fieldName: 'Status', fieldValue: changeControl.status },
        { fieldName: 'Changer', fieldValue: changeControl.changer },
        { fieldName: 'Date Changed', fieldValue: changeControl.dateChanged },
        { fieldName: 'Changer Notes', fieldValue: changeControl.changerComments },
        { fieldName: 'Approver', fieldValue: changeControl.approver },
        { fieldName: 'Approver Notes', fieldValue: changeControl.approverComments },
      ],
    }]
  },
  {
    tabTitle: ENTITY_APPROVING_DIALOG_TABS.BEFORE_CHANGES,
    tabSections: changeControl.entityBefore ? [
      { sectionTitle: 'Entity Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['Entity Details'] },
      { sectionTitle: 'SWIFT Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['SWIFT Details'] },
      {
        sectionTitle: `${changeControl.entityBefore.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(changeControl.entityBefore)['Routing Details']
      }
    ] : []
  },
  {
    tabTitle: ENTITY_APPROVING_DIALOG_TABS.AFTER_CHANGES,
    tabSections: [
      { sectionTitle: 'Entity Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['Entity Details'] },
      { sectionTitle: 'SWIFT Details', sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['SWIFT Details'] },
      {
        sectionTitle: `${changeControl.entityLog.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(changeControl.entityLog)['Routing Details']
      }
    ]
  },
  {
    tabTitle: ENTITY_APPROVING_DIALOG_TABS.DIFFERENCES,
    tabSections: changeControl.entityBefore ? [
      {
        sectionTitle: 'Entity Details',
        sectionItems: getEntityDetailsSectionItems(difference(changeControl.entityLog, changeControl.entityBefore))['Entity Details']
      },
      {
        sectionTitle: 'SWIFT Details',
        sectionItems: getEntityDetailsSectionItems(difference(changeControl.entityLog, changeControl.entityBefore))['SWIFT Details']
      },
      {
        sectionTitle: `${changeControl.entityBefore.service} Routing Details`,
        sectionItems: getEntityDetailsSectionItems(difference(changeControl.entityLog, changeControl.entityBefore))['Routing Details']
      }
    ] : []
  }
].filter(el => !isEmpty(el.tabSections));
