import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { formatDate, formatNumber } from '@angular/common';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';

export const TRANSACTION_SEARCH_DISPLAY_NAMES = {
    id: 'ID',
    entity: 'Entity',
    service: 'Service',
    direction: 'Direction',
    trxStatus: 'Transaction Status',
    reference: 'Reference',
    transactionID: 'Transaction ID',
    paymentBIC: 'Payment BIC',
    type: 'Type',
    from: 'From',
    to: 'To',
    settleDate: 'Settle Date',
    settleAmount: 'Settle Amount',
    status: 'Status',
    filename: 'Filename',
    file: 'File',
    fileID: 'File ID',
    workflowID: 'WFID',
    timestamp: 'Timestamp'
};

export const getTransactionSearchDisplayName = (key: string) => TRANSACTION_SEARCH_DISPLAY_NAMES[key] || key;

export const getTransactionDetailsTabs = (transaction: Transaction, actionMapping?: {[id: string]: any}): Tab[] => {
    const mapping = actionMapping || {};
    return [
        {
            tabTitle: 'Transaction Details',
            tabSections: [{
                sectionItems: [
                    { fieldName: 'id', fieldValue: transaction.id },
                    { fieldName: 'entity', fieldValue: transaction.entity },
                    { fieldName: 'paymentBIC', fieldValue: transaction.paymentBIC },
                    { fieldName: 'file', fieldValue: transaction.filename, isActionButton: 'file' in mapping },
                    { fieldName: 'reference', fieldValue: transaction.reference },
                    { fieldName: 'transactionID', fieldValue: transaction.transactionID, isActionButton: 'transactionID' in mapping },
                    { fieldName: 'type', fieldValue: transaction.type },
                    { fieldName: 'direction', fieldValue: getDirectionStringValue(transaction.isoutbound) },
                    { fieldName: 'timestamp', fieldValue: formatDate(transaction.timestamp, 'dd/MM/yyyy, HH:mm', 'en-GB') },
                    { fieldName: 'workflowID', fieldValue: transaction.workflowID, isActionButton: 'workflowID' in mapping },
                    { fieldName: 'settleDate', fieldValue: formatDate(transaction.settleDate, 'dd/MM/yyyy, HH:mm', 'en-GB') },
                    { fieldName: 'settleAmount', fieldValue: formatNumber(transaction.settleAmount, 'en-GB', '1.2-2') },
                    { fieldName: 'status', fieldValue: transaction.status },
                ]
            }]
        }
    ].filter(el => el);
};

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
