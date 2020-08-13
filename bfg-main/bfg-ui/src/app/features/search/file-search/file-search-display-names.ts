import { Tab, TableActions } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { File } from 'src/app/shared/models/file/file.model';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { TransactionsWithPagination } from 'src/app/shared/models/file/transactions-with-pagination.model';
import { Transaction } from 'src/app/shared/models/file/transaction.model';
import { FileError } from 'src/app/shared/models/file/file-error.model';

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
    settleAmount: 'Settle Amount',
    settleDate: 'Settle Date',
    transactionID: 'Transaction ID',
    code: 'Error Code',
    name: 'Name',
    description: 'Description'
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
        { fieldName: 'timestamp', fieldValue: file.timestamp },
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

export const getFileTransactionsTabs = (transactionList: TransactionsWithPagination, tableActions: TableActions): Tab[] => [
    {
        tabTitle: 'Transactions',
        tabSections: [],
        tableObject: {
            tableColumns: ['id', 'transactionID', 'settleDate', 'settleAmount', 'type', 'status', 'workflowID'],
            tableDataSource: transactionList.content,
            tableActions
        }
    }
].filter(el => el);

export const getTransactionDetailsTabs = (transaction: Transaction): Tab[] => [
    {
        tabTitle: 'Transaction Details',
        tabSections: [{
            sectionItems: [
                { fieldName: 'id', fieldValue: transaction.id },
                { fieldName: 'transactionID', fieldValue: transaction.transactionID },
                { fieldName: 'settleDate', fieldValue: transaction.settleDate },
                { fieldName: 'settleAmount', fieldValue: transaction.settleAmount },
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
