import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';

export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    serialNumber: 'Serial Number',
    thumbprint: 'SHA-1 Thumbprint',
    validDates: 'Valid Dates',
    startDate: 'Start Date',
    endDate: 'End Date',
    issuer: 'Issuer',
    subject: 'Subject',
    CN: 'Common Name',
    OU: 'Organization Unit',
    O: 'Organization',
    L: 'Locality',
    C: 'Country',
    ST: 'State or Province',
    changerComments: 'Changer Comments',
    authChainReport: 'Auth Chain Report',
    subjectDN: 'Subject DN',
    certificateName: 'Certificate Name'
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;

export const getTrustedCertificateItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`);

const getTrustedCertificateDetailsSectionItems = (trustedCertificate: TrustedCertificate) => ({
    'Trusted Certificate Details': [
        { fieldName: getTrustedCertificateDisplayName('certificateName'), fieldValue: trustedCertificate.certificateName },
        { fieldName: getTrustedCertificateDisplayName('serialNumber'), fieldValue: trustedCertificate.serialNumber },
        { fieldName: getTrustedCertificateDisplayName('thumbprint'), fieldValue: trustedCertificate.thumbprint },
        { fieldName: getTrustedCertificateDisplayName('startDate'), fieldValue: trustedCertificate.startDate },
        { fieldName: getTrustedCertificateDisplayName('endDate'), fieldValue: trustedCertificate.endDate },
        {
            fieldName: getTrustedCertificateDisplayName('issuer'),
            fieldValue: getTrustedCertificateItemInfoValues(trustedCertificate.issuer)
        },
        {
            fieldName: getTrustedCertificateDisplayName('subject'),
            fieldValue: getTrustedCertificateItemInfoValues(trustedCertificate.subject)
        },
    ],
});

export const getTrustedCertificateDetailsTabs = (trustedCertificate: TrustedCertificate): Tab[] => [
    {
        tabTitle: 'Entity Details',
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(trustedCertificate)['Trusted Certificate Details'] }]
    }
].filter(el => el);
