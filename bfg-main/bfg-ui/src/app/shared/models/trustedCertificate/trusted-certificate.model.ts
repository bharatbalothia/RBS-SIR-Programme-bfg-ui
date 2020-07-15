
export interface TrustedCertificate {
    serialNumber: string;
    thumbprint: string;
    thumbprint256: string;
    startDate: Date;
    endDate: Date;
    issuer: TSItemInfo;
    subject: TSItemInfo;
    authChainReport: AuthChainReport[];
    valid: boolean;
    changerComments: string;
}

export interface TSItemInfo {
    C: string[];
    O: string[];
    ST: string[];
    OU: string[];
    CN: string[];
    L: string[];
}

export interface AuthChainReport {
    subjectDN: string;
    certificateName: string;
}
