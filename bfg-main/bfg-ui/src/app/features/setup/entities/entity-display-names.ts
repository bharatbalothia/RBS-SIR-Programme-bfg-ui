import { Entity } from 'src/app/shared/entity/entity.model';
import { Section } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { ChangeControl } from 'src/app/shared/entity/change-control.model';

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

export const getEntityDetailsFields = (entity: Entity): Section[] => [
    {
        sectionTitle: 'Entity Details',
        sectionItems: [
          { fieldName: getEntityDisplayName('entityId'), fieldValue: entity.entityId },
          { fieldName: getEntityDisplayName('entity'), fieldValue: entity.entity },
          { fieldName: getEntityDisplayName('service'), fieldValue: entity.service, shouldDisplayValueUpperCase: true },
        ]
      },
      {
        sectionTitle: 'SWIFT Details',
        sectionItems: [
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
        ]
      },
      {
        sectionTitle: `${entity.service} Routing Details`,
        sectionItems: [
          { fieldName: getEntityDisplayName('inboundRequestorDN'), fieldValue: entity.inboundRequestorDN },
          { fieldName: getEntityDisplayName('inboundResponderDN'), fieldValue: entity.inboundResponderDN },
          { fieldName: getEntityDisplayName('inboundService'), fieldValue: entity.inboundService },
          { fieldName: getEntityDisplayName('inboundRequestType'), fieldValue: entity.inboundRequestType },
        ]
      }
];

export const getPendingChangesFields = (changeControl: ChangeControl): Section[] => {
  return [
    {
      sectionTitle: 'Change Details',
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
    }
  ];
}
