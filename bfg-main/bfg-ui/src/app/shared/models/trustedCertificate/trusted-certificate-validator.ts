import { Injectable } from '@angular/core';
import { ValidatorFn, FormGroup, AsyncValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { TrustedCertificate } from './trusted-certificate.model';
import { TrustedCertificateService } from './trusted-certificate.service';

@Injectable({
    providedIn: 'root'
})
export class TrustedCertificateValidators {

    constructor(private trustedCertificateService: TrustedCertificateService) { }


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

    trustedCertificateExistsValidator(): AsyncValidatorFn {
        return (control: AbstractControl): Observable<ValidationErrors | null> => {
            return this.trustedCertificateService.isTrustedCertificateNameExists(control.value).pipe(
                take(1),
                map(exists => {
                    return exists ? { nameExists: exists } : null;
                }), catchError(() => of(null))
            );
        };
    }


}
