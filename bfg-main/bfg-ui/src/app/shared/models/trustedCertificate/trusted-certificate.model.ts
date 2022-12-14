import { ErrorsField } from 'src/app/core/utils/error-template';

export interface TrustedCertificate {
    certificateId?: string;
    certificateLogId?: string;
    certificateName: string;
    serialNumber: string;
    thumbprint: string;
    thumbprint256: string;
    from: Date;
    endDate: Date;
    issuer: TSItemInfo;
    subject: TSItemInfo;
    authChainReport: AuthChainReport[];
    valid: boolean;
    changerComments: string;
    certificateErrors?: ErrorsField[];
    certificateWarnings?: ErrorsField[];
}

export interface TSItemInfo {
    C: string[];
    O: string[];
    ST: string[];
    OU: string[];
    CN: string[];
    L: string[];
    EMAILADDRESS: string[];
}

export interface AuthChainReport {
    subjectDN: string;
    certificateName: string;
}
