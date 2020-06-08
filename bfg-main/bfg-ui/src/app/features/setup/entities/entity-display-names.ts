import { Entity } from 'src/app/shared/entity/entity.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { ChangeControl } from 'src/app/shared/entity/change-control.model';
import { isEmpty } from 'lodash';
import { difference } from 'src/app/shared/utils/utils';
import { ENTITY_APPROVING_DIALOG_TABS } from './entity-approving-dialog/entity-approving-dialog-tabs';

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
  serviceName: 'Service Name',
};

export const getEntityDisplayName = (key: string) => ENTITY_DISPLAY_NAMES[key] || key;

const getEntityDetailsSectionItems = (entity) => ({
  'Entity Details': [
    { fieldName: getEntityDisplayName('entityId'), fieldValue: entity.entityId },
    { fieldName: getEntityDisplayName('entity'), fieldValue: entity.entity },
    { fieldName: getEntityDisplayName('service'), fieldValue: entity.service, shouldDisplayValueUpperCase: true },
  ],
  'SWIFT Details': [
    { fieldName: getEntityDisplayName('requestorDN'), fieldValue: entity.requestorDN },
    { fieldName: getEntityDisplayName('responderDN'), fieldValue: entity.responderDN },
    { fieldName: getEntityDisplayName('serviceName'), fieldValue: entity.serviceName },
    { fieldName: getEntityDisplayName('requestType'), fieldValue: entity.requestType },
    { fieldName: getEntityDisplayName('snF'), fieldValue: entity.snF },
    { fieldName: getEntityDisplayName('trace'), fieldValue: entity.trace },
    { fieldName: getEntityDisplayName('deliveryNotification'), fieldValue: entity.deliveryNotification },
    { fieldName: getEntityDisplayName('nonRepudiation'), fieldValue: entity.nonRepudiation },
    { fieldName: getEntityDisplayName('e2eSigning'), fieldValue: entity.e2eSigning },
    { fieldName: getEntityDisplayName('deliveryNotifDN'), fieldValue: entity.deliveryNotifDN },
    { fieldName: getEntityDisplayName('deliveryNotifRT'), fieldValue: entity.deliveryNotifRT },
    { fieldName: getEntityDisplayName('requestRef'), fieldValue: entity.requestRef },
    { fieldName: getEntityDisplayName('fileDesc'), fieldValue: entity.fileDesc },
    { fieldName: getEntityDisplayName('fileInfo'), fieldValue: entity.fileInfo },
    { fieldName: getEntityDisplayName('transferInfo'), fieldValue: entity.transferInfo },
    { fieldName: getEntityDisplayName('transferDesc'), fieldValue: entity.transferDesc },
  ],
  'Routing Details': [
    { fieldName: getEntityDisplayName('inboundRequestorDN'), fieldValue: entity.inboundRequestorDN },
    { fieldName: getEntityDisplayName('inboundResponderDN'), fieldValue: entity.inboundResponderDN },
    { fieldName: getEntityDisplayName('inboundService'), fieldValue: entity.inboundService },
    { fieldName: getEntityDisplayName('inboundRequestType'), fieldValue: entity.inboundRequestType },
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
