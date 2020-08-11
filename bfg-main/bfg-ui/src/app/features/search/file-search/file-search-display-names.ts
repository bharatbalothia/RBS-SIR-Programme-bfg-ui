import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { File } from 'src/app/shared/models/file/file.model';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';

export const FILE_SEARCH_DISPLAY_NAMES = {
    entityID: 'Entity',
    service: 'Service',
    direction: 'Direction',
    fileStatus: 'File Status',
    bpState: 'BP State',
    fileName: 'Filename',
    reference: 'Reference',
    type: 'Type',
    from: 'From',
    to: 'To',
    id: 'File ID',
    timestamp: 'Timestamp',
    workflowID: 'WFID',
    errorCode: 'Error',
    transactionTotal: 'Transactions',
    messageID: 'Message ID',
    status: 'Status',
};

export const getFileSearchDisplayName = (key: string) => FILE_SEARCH_DISPLAY_NAMES[key] || key;

const getFileDetailsSectionItems = (file: File) => ({
    'File Details': [
        { fieldName: getFileSearchDisplayName('id'), fieldValue: file.id },
        { fieldName: getFileSearchDisplayName('entityID'), fieldValue: file.entityID },
        { fieldName: getFileSearchDisplayName('filename'), fieldValue: file.filename },
        { fieldName: getFileSearchDisplayName('reference'), fieldValue: file.reference },
        { fieldName: getFileSearchDisplayName('service'), fieldValue: file.service },
        { fieldName: getFileSearchDisplayName('type'), fieldValue: file.type },
        { fieldName: getFileSearchDisplayName('direction'), fieldValue: getDirectionStringValue(file.outbound) },
        { fieldName: getFileSearchDisplayName('timestamp'), fieldValue: file.timestamp },
        { fieldName: getFileSearchDisplayName('workflowID'), fieldValue: file.workflowID },
        { fieldName: getFileSearchDisplayName('messageID'), fieldValue: file.messageID },
        { fieldName: 'status', fieldValue: file.status },
        { fieldName: 'errorCode', fieldValue: file.errorCode, isActionButton: true },
        { fieldName: 'transactionTotal', fieldValue: file.transactionTotal, isActionButton: true }
    ],
});

export const getFileDetailsTabs = (file: File): Tab[] => [
    {
        tabTitle: 'File Details',
        tabSections: [{ sectionItems: getFileDetailsSectionItems(file)['File Details'] }]
    }
].filter(el => el);
