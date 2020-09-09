import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { File } from 'src/app/shared/models/file/file.model';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { FileError } from 'src/app/shared/models/file/file-error.model';
import { formatDate } from '@angular/common';

export const FILE_SEARCH_DISPLAY_NAMES = {
    service: 'Service',
    direction: 'Direction',
    fileStatus: 'File Status',
    bpState: 'BP State',
    filename: 'Filename',
    reference: 'Reference',
    type: 'Type',
    from: 'From',
    to: 'To',
    id: 'ID',
    fileID: 'File ID',
    timestamp: 'Timestamp',
    workflowID: 'WFID',
    errorCode: 'Error',
    transactionTotal: 'Transactions',
    messageID: 'Message ID',
    status: 'Status',
    settleAmount: 'Settle Amount',
    settleDate: 'Settle Date',
    transactionID: 'Transaction ID',
    code: 'Error Code',
    name: 'Name',
    description: 'Description',
    paymentBIC: 'Payment BIC',
    entity: 'Entity',
    processID: 'Process ID',
    document: 'Document'
};

export const getFileSearchDisplayName = (key: string) => FILE_SEARCH_DISPLAY_NAMES[key] || key;

const getFileDetailsSectionItems = (file: File) => ({
    'File Details': [
        { fieldName: 'id', fieldValue: file.id },
        { fieldName: 'filename', fieldValue: file.filename, isActionButton: true },
        { fieldName: 'entity', fieldValue: file.entity.entity, isActionButton: true },
        { fieldName: 'reference', fieldValue: file.reference },
        { fieldName: 'service', fieldValue: file.service },
        { fieldName: 'type', fieldValue: file.type },
        { fieldName: 'direction', fieldValue: getDirectionStringValue(file.outbound) },
        { fieldName: 'timestamp', fieldValue: formatDate(file.timestamp, 'dd/MM/yyyy, HH:mm', 'en-GB') },
        { fieldName: 'workflowID', fieldValue: file.workflowID },
        { fieldName: 'messageID', fieldValue: file.messageID },
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

export const getErrorDetailsTabs = (fileError: FileError): Tab[] => [
    {
        tabTitle: 'Error Details',
        tabSections: [{
            sectionItems: [
                { fieldName: 'code', fieldValue: fileError.code },
                { fieldName: 'name', fieldValue: fileError.name },
                { fieldName: 'description', fieldValue: fileError.description },
            ]
        }]
    }
].filter(el => el);
