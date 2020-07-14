import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';

export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    name: 'Name',
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
    changerComments: 'Changer Comments'
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;

export const getTSItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`);

const getTrustedCertificateDetailsSectionItems = (trustedCertificate: TrustedCertificate) => ({
    'Trusted Certificate Details': [
        { fieldName: getTrustedCertificateDisplayName('name'), fieldValue: trustedCertificate.name },
        { fieldName: getTrustedCertificateDisplayName('serialNumber'), fieldValue: trustedCertificate.serialNumber },
        { fieldName: getTrustedCertificateDisplayName('thumbprint'), fieldValue: trustedCertificate.thumbprint },
        { fieldName: getTrustedCertificateDisplayName('startDate'), fieldValue: trustedCertificate.startDate },
        { fieldName: getTrustedCertificateDisplayName('endDate'), fieldValue: trustedCertificate.endDate },
        { fieldName: getTrustedCertificateDisplayName('issuer'), fieldValue: getTSItemInfoValues(trustedCertificate.issuer) },
        { fieldName: getTrustedCertificateDisplayName('subject'), fieldValue: getTSItemInfoValues(trustedCertificate.subject) },
    ],
});

export const getTrustedCertificateDetailsTabs = (trustedCertificate: TrustedCertificate): Tab[] => [
    {
        tabTitle: 'Entity Details',
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(trustedCertificate)['Trusted Certificate Details'] }]
    }
].filter(el => el);
