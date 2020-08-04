import { Entity } from '../entity/entity.model';
import { TrustedCertificate } from '../trustedCertificate/trusted-certificate.model';

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
    entityLog?: Entity;
    trustedCertificateLog?: TrustedCertificate;
    entityBefore?: Entity;
    certificateBefore?: TrustedCertificate;
    pending: boolean;
    statusText: string;
    shortType: string;
}
