import { Injectable } from '@angular/core';
import { ValidatorFn, FormGroup, Validators } from '@angular/forms';
import { TrustedCertificate } from './trusted-certificate.model';

@Injectable({
    providedIn: 'root'
})
export class TrustedCertificateValidators {

    constructor() { }


    isTrustedCertificateHasErrors(trustedCertificate: TrustedCertificate): ValidatorFn {
        return (control: FormGroup) => {
            if (trustedCertificate.certificateErrors) {
                return ({ invalid: true });
            }
            else {
                return null;
            }
        };
    }

}
