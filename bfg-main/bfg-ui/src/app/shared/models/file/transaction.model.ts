export interface Transaction {
    id: number;
    settleAmount: number;
    settleDate: string;
    status: number;
    transactionID: string;
    type: string;
    workflowID: number;
}
