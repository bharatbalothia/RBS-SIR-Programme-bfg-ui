import { Entity } from '../entity/entity.model';

export interface File {
    docID: string;
    entity: Entity;
    errorCode: string;
    filename: string;
    id: number;
    messageID: number;
    outbound: boolean;
    override: boolean;
    reference: string;
    service: string;
    status: number;
    statusLabel: string;
    timestamp: string;
    transactionTotal: number;
    type: string;
    workflowID: number;
}
