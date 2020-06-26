export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    name: 'Name',
    serialNumber: 'Serial Number',
    thumbprint: 'SHA-1 Thumbprint',
    validDates: 'Valid Dates',
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