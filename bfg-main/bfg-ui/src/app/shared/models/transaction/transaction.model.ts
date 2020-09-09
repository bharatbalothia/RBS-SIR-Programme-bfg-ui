import { Entity } from '../entity/entity.model';

export interface Transaction {
    id: number;
    settleAmount: number;
    settleDate: string;
    entity: Entity;
    paymentBIC: string;
    filename: string;
    reference: string;
    isoutbound: boolean;
    status: number;
    transactionID: string;
    type: string;
    workflowID: number;
    fileID: number;
    docID: string;
}
