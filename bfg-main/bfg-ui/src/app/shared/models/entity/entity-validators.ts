import { Injectable } from '@angular/core';
import { AbstractControl, ValidationErrors, AsyncValidatorFn, ValidatorFn, FormGroup, Validators } from '@angular/forms';
import { EntityService } from './entity.service';
import { Observable, of } from 'rxjs';
import { map, catchError, take } from 'rxjs/operators';
import { BIC11, BIC8 } from 'src/app/core/constants/validation-regexes';
import { MatHorizontalStepper } from '@angular/material/stepper';

@Injectable({
  providedIn: 'root'
})
export class EntityValidators {

  constructor(private entityService: EntityService) { }

  entityExistsValidator(service: AbstractControl): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.entityService.isEntityExists(service.value, control.value).pipe(
        take(1),
        map(exists => {
          return exists ? { entityExists: true } : null;
        }), catchError(() => of(null))
      );
    };
  }

  mailboxPathOutValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.entityService.isMailboxPathOutExists(control.value).pipe(
        take(1),
        map(exists => {
          return exists ? { mailboxPathOutExists: true } : null;
        }), catchError(() => of(null))
      );
    };
  }

  mqQueueOutValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.entityService.isMqQueueOutExists(control.value).pipe(
        take(1),
        map(exists => {
          return exists ? { mqQueueOutExists: true } : null;
        }), catchError(() => of(null))
      );
    };
  }

  entityPatternByServiceValidator(service: AbstractControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const pattern = service.value === 'GPL' ? BIC11 : BIC8;
      const error = service.value === 'GPL' ? 'patternBIC11' : 'patternBIC8';
      const match = pattern.test(control.value);
      return match ? null : { [error]: true };
    };
  }

  directParticipantValidator(): ValidatorFn {
    return (control: FormGroup): null => {
      const participantType = control.get('entityParticipantType');
      const directParticipant = control.get('directParticipant');

      if (participantType.value === 'INDIRECT' && directParticipant.validator == null) {
        directParticipant.setValidators(Validators.required);
        directParticipant.updateValueAndValidity();
      } else if (participantType.value !== 'INDIRECT' && directParticipant.validator != null) {
        directParticipant.clearValidators();
        directParticipant.updateValueAndValidity();
      }
      return null;
    };
  }

  directParticipantValidatorRange(directParticipantList: string[]): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (!control.value || directParticipantList.includes(control.value)) {
        return null;
      } else {
        control.markAsTouched();
        return { directParticipantRange: true };
      }
    };
  }

}
