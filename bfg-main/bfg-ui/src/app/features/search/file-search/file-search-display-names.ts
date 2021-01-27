import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { File } from 'src/app/shared/models/file/file.model';
import { FileError } from 'src/app/shared/models/file/file-error.model';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';
import { getDirectionIcon } from '../transaction-search/transaction-search-display-names';
import { titleCase } from 'src/app/shared/utils/utils';

export const FILE_SEARCH_DISPLAY_NAMES = {
    service: 'Service',
    direction: 'Direction',
    fileStatus: 'File Status',
    bpstate: 'BP State',
    filename: 'Filename',
    file: 'File',
    reference: 'Reference',
    type: 'Type',
    from: 'From',
    to: 'To',
    id: 'ID',
    fileID: 'File ID',
    timestamp: 'Timestamp',
    workflowID: 'WFID',
    errorCode: 'Error Code',
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
        { fieldName: 'fileID', fieldValue: file.id },
        { fieldName: 'entity', fieldValue: file.entity.entity || 'None', isActionButton: !!file.entity.entity },
        { fieldName: 'filename', fieldValue: file.filename, isActionButton: true },
        { fieldName: 'reference', fieldValue: file.reference },
        { fieldName: 'service', fieldValue: file.service },
        { fieldName: 'type', fieldValue: file.type },
        { fieldName: 'direction', fieldValue: titleCase(file.direction), icon: getDirectionIcon(file.direction) },
        { fieldName: 'timestamp', fieldValue: file.timestamp },
        { fieldName: 'workflowID', fieldValue: file.workflowID, isActionButton: true },
        { fieldName: 'messageID', fieldValue: file.messageID },
        { fieldName: 'status', fieldValue: file.statusLabel || file.status + ' [no description available]' },
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

export const getFileDocumentInfoTabs = (documentContent: DocumentContent): Tab[] => [
    {
        tabTitle: 'Message Contents',
        tabSections: [{
            sectionItems: [
                { fieldName: 'document', fieldValue: documentContent.document, isXML: true },
            ].filter(el => el.fieldValue)
        }],
        noContentLabel: { label: 'No Message matches your selection criteria.', icon: 'warning' }
    }
].filter(el => el);
