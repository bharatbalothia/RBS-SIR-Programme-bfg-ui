
export interface TrustedCertificate {
    serialNumber: string;
    thumbprint: string;
    startDate: Date;
    endDate: Date;
    issuer: {
        C: string[];
        O: string[];
        OU: string[];
        CN: string[];
        L: string[];
    };
    subject: {
        ST: string[];
        C: string[];
        CN: string[];
        L: string[];
        O: string[];
    };
    authChainReport: AuthChainReport[];
    valid: boolean;
    changerComments: string;
}

export interface AuthChainReport {
    subjectDN: string;
    certificateName: string;
}
