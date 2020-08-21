import { Tab, TableActions } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { File } from 'src/app/shared/models/file/file.model';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { TransactionsWithPagination } from 'src/app/shared/models/file/transactions-with-pagination.model';
import { Transaction } from 'src/app/shared/models/file/transaction.model';
import { FileError } from 'src/app/shared/models/file/file-error.model';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';
import { formatDate } from '@angular/common';

export const FILE_SEARCH_DISPLAY_NAMES = {
    entityID: 'Entity',
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
        { fieldName: 'entityID', fieldValue: file.entityID },
        { fieldName: 'filename', fieldValue: file.filename },
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

export const getTransactionDetailsTabs = (transaction: Transaction): Tab[] => [
    {
        tabTitle: 'Transaction Details',
        tabSections: [{
            sectionItems: [
                { fieldName: 'id', fieldValue: transaction.id },
                { fieldName: 'transactionID', fieldValue: transaction.transactionID, isActionButton: true },
                { fieldName: 'settleDate', fieldValue: formatDate(transaction.settleDate, 'dd/MM/yyyy, HH:mm', 'en-GB') },
                { fieldName: 'settleAmount', fieldValue: transaction.settleAmount },
                { fieldName: 'entity', fieldValue: transaction.entity },
                { fieldName: 'paymentBIC', fieldValue: transaction.paymentBIC },
                { fieldName: 'filename', fieldValue: transaction.filename },
                { fieldName: 'fileID', fieldValue: transaction.fileID },
                { fieldName: 'reference', fieldValue: transaction.reference },
                { fieldName: 'direction', fieldValue: getDirectionStringValue(transaction.isoutbound) },
                { fieldName: 'type', fieldValue: transaction.type },
                { fieldName: 'status', fieldValue: transaction.status },
                { fieldName: 'workflowID', fieldValue: transaction.workflowID },
            ]
        }]
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

export const getTransactionDocumentInfoTabs = (documentContent: DocumentContent): Tab[] => [
    {
        tabTitle: 'Document Info',
        tabSections: [{
            sectionItems: [
                { fieldName: 'processID', fieldValue: documentContent.processID },
                { fieldName: 'document', fieldValue: documentContent.document, isXML: true },
            ]
        }]
    }
].filter(el => el);
