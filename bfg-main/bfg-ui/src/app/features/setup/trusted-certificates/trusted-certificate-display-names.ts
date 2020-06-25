export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    name: 'Name',
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;