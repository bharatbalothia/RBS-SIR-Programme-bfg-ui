import { getTrustedCertificateDisplayName } from './trusted-certificate-display-names';

export const TRUSTED_CERTIFICATE_VALIDATION_MESSAGES = {
    name: [
        { type: 'required', message: `${getTrustedCertificateDisplayName('name')} is required` },
        {
            type: 'pattern',
            message: `${getTrustedCertificateDisplayName('name')} can only contain the following characters: a-zA-Z0-9 -_:`
        },
    ]
};
