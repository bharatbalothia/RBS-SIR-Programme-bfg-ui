import { Entity } from './entity.model';

export interface ChangeControl {
    changeID: string;
    operation: string;
    status: number;
    objectType: string;
    objectKey: string;
    changer: string;
    dateChanged: Date;
    approver: string;
    dateApproved: Date;
    changerComments: string;
    approverComments: string;
    resultMeta1: string;
    resultMeta2: string;
    resultMeta3: null;
    entityLog: Entity;
    entityBefore?: Entity;
    pending: boolean;
    statusText: string;
    shortType: string;
}
