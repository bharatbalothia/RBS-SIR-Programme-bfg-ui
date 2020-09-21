import { Tab } from '../../components/details-dialog/details-dialog-data.model';
import { BusinessProcess } from './business-process.model';

export const BUSINESS_PROCESS_DISPLAY_NAMES = {
    advStatus: 'Advanced Status',
    docId: 'Document',
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



};

export const getBusinessProcessDisplayName = (key: string) => BUSINESS_PROCESS_DISPLAY_NAMES[key] || key;

const getBusinessProcessDetailsSectionItems = (businessProcess: BusinessProcess) => ({
    'Business Process Details': [
        { fieldName: 'name', fieldValue: businessProcess.name },
        { fieldName: 'documentTracking', fieldValue: businessProcess.documentTracking },
        { fieldName: 'startMode', fieldValue: businessProcess.startMode },
        { fieldName: 'queue', fieldValue: businessProcess.queue },
        { fieldName: 'recoveryLevel', fieldValue: businessProcess.recoveryLevel },
        { fieldName: 'softstopRecoveryLevel', fieldValue: businessProcess.softstopRecoveryLevel },
        { fieldName: 'lifespanDays', fieldValue: businessProcess.lifespanDays },
        { fieldName: 'lifespanHours', fieldValue: businessProcess.lifespanHours },
    ],
});

export const getBusinessProcessDetailsTabs = (businessProcess: BusinessProcess): Tab[] => [
    {
        tabTitle: 'Business Process Details',
        tabSections: [{ sectionItems: getBusinessProcessDetailsSectionItems(businessProcess)['Business Process Details'] }]
    }
].filter(el => el);