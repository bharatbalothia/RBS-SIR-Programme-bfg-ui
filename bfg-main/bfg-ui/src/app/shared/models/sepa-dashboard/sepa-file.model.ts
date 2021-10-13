export interface SEPAFile {
    filename: string;
    id: number;
    direction: string;
    docID: string;
    settleAmountTotal: number;
    transactionTotal: number;
    type: string;
}
