import { Tab } from '../../components/details-dialog/details-dialog-data.model';
import { capitalizeFirstLetter, removeEmpties } from '../../utils/utils';
import { BusinessProcessDocumentContent } from './business-process-document-content.model';
import { BusinessProcess } from './business-process.model';

export const BUSINESS_PROCESS_DISPLAY_NAMES = {
    advStatus: 'Advanced Status',
    docId: 'Document',
    instanceData: 'Instance Data',
    endTime: 'Ended',
    exeState: 'Status',
    nodeExecuted: 'Execution Node',
    serviceName: 'Service',
    startTime: 'Started',
    statusRpt: 'Status Report',
    stepId: 'Step',
    name: 'Business Process Name',
    documentTracking: 'Document Tracking',
    startMode: 'Start Mode',
    queue: 'Queue',
    recoveryLevel: 'Recovery Level',
    softstopRecoveryLevel: 'Softstop Recovery Level',
    lifespanDays: 'Lifespan Days',
    lifespanHours: 'Lifespan Hours',
    eventReportingLevel: 'Event Reporting Level',
    wfdVersion: 'Version',
    onfaultProcessing: 'Set onfault processing',
    enableTransaction: 'Transaction',
    persistenceLevel: 'Persistence Level',
    documentStorage: 'Document Storage Type',
    expedite: 'Expedite',
    deadline: 'Complete by - Deadline',
    secondNotification: 'Second Notification',
    firstNotification: 'First Notification',
    commitStepsUponError: 'Commit On Error',
    description: 'Description',
    businessProcess: 'Business Process Definition',
    instanceId: 'Instance ID',
    processName: 'Process Name',
    documentName: 'Document Name',
    storageType: 'Document Store',
    documentId: 'Document ID',
    documentPayload: 'Document in the message from service',
    removalMethod: 'Removal Method'
};

export const getBusinessProcessDisplayName = (key: string) => BUSINESS_PROCESS_DISPLAY_NAMES[key] || key;

const getBusinessProcessDetailsSectionItems = (businessProcess: BusinessProcess) => ({
    'Business Process Details': [
        { fieldName: 'name', fieldValue: businessProcess.name },
        { fieldName: 'documentTracking', fieldValue: capitalizeFirstLetter(businessProcess.documentTracking) },
        { fieldName: 'startMode', fieldValue: businessProcess.startMode },
        { fieldName: 'queue', fieldValue: businessProcess.queue },
        { fieldName: 'recoveryLevel', fieldValue: businessProcess.recoveryLevel },
        { fieldName: 'softstopRecoveryLevel', fieldValue: businessProcess.softstopRecoveryLevel },
        { fieldName: 'lifespanDays', fieldValue: businessProcess.lifespanDays },
        { fieldName: 'lifespanHours', fieldValue: businessProcess.lifespanHours },
        { fieldName: 'removalMethod', fieldValue: businessProcess.removalMethod },
        { fieldName: 'eventReportingLevel', fieldValue: businessProcess.eventReportingLevel },
        { fieldName: 'wfdVersion', fieldValue: businessProcess.wfdVersion },
        { fieldName: 'onfaultProcessing', fieldValue: capitalizeFirstLetter(businessProcess.onfaultProcessing) },
        { fieldName: 'enableTransaction', fieldValue: capitalizeFirstLetter(businessProcess.enableTransaction) },
        { fieldName: 'persistenceLevel', fieldValue: businessProcess.persistenceLevel },
        { fieldName: 'documentStorage', fieldValue: businessProcess.documentStorage },
        { fieldName: 'expedite', fieldValue: businessProcess.expedite },
        {
            fieldName: 'deadline', fieldValue: `${businessProcess.deadlineHours ? `${businessProcess.deadlineHours} Hours ` : ''}` +
                `${businessProcess.deadlineMinutes ? ` ${businessProcess.deadlineMinutes} Minutes` : ''}`
        },
        {
            fieldName: 'firstNotification',
            fieldValue: `${businessProcess.firstNotificationHours ? `${businessProcess.firstNotificationHours} Hours ` : ''}` +
                `${businessProcess.firstNotificationMinutes ? ` ${businessProcess.firstNotificationMinutes} Minutes` : ''}`
        },
        {
            fieldName: 'secondNotification',
            fieldValue: `${businessProcess.secondNotificationHours ? `${businessProcess.secondNotificationHours} Hours ` : ''}` +
                `${businessProcess.secondNotificationMinutes ? ` ${businessProcess.secondNotificationMinutes} Minutes` : ''}`
        },
        { fieldName: 'commitStepsUponError', fieldValue: capitalizeFirstLetter(businessProcess.commitStepsUponError) },
        { fieldName: 'description', fieldValue: businessProcess.description },
        { fieldName: 'businessProcess', fieldValue: businessProcess.businessProcess, isXML: true },
    ].map(el => removeEmpties(el))
});

export const getBusinessProcessDetailsTabs = (businessProcess: BusinessProcess): Tab[] => [
    {
        tabTitle: 'Business Process Details',
        tabSections: [{ sectionItems: getBusinessProcessDetailsSectionItems(businessProcess)['Business Process Details'] }]
    }
].filter(el => el);

export const getBusinessProcessDocumentInfoTabs = (documentContent: BusinessProcessDocumentContent): Tab[] => [
    {
        tabTitle: 'Document Details',
        tabSections: [{
            sectionItems: [
                { fieldName: 'processName', fieldValue: documentContent.processName },
                { fieldName: 'instanceId', fieldValue: documentContent.workflowId },
                { fieldName: 'serviceName', fieldValue: documentContent.serviceName },
                { fieldName: 'documentName', fieldValue: documentContent.documentName },
                { fieldName: 'storageType', fieldValue: documentContent.storageType },
                { fieldName: 'documentId', fieldValue: documentContent.documentId },
                { fieldName: 'documentPayload', fieldValue: documentContent.documentPayload, isXML: true },
            ]
        }]
    }
].filter(el => el);
