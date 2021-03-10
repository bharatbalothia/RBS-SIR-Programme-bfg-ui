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
    statusLabel: string;
    transactionID: string;
    type: string;
    workflowID: number;
    fileID: number;
    docID: string;
    timestamp: string;
    direction: string;
    payaway: boolean;
}
