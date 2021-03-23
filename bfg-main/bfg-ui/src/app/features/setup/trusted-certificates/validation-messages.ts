import { getTrustedCertificateDisplayName } from './trusted-certificate-display-names';

export const TRUSTED_CERTIFICATE_VALIDATION_MESSAGES = {
    name: [
        { type: 'required', message: `A ${getTrustedCertificateDisplayName('name')} is required for the Trusted Certificate` },
        {
            type: 'pattern',
            message: `The Trusted Certificate ${getTrustedCertificateDisplayName('name')} can only contain the following characters: a-z A-Z 0-9 -_:.`
        },
    ]
};
