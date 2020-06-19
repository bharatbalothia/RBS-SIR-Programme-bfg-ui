import { Injectable } from '@angular/core';
import { AbstractControl, ValidationErrors, AsyncValidatorFn, ValidatorFn, FormGroup, Validators } from '@angular/forms';
import { EntityService } from './entity.service';
import { Observable, of } from 'rxjs';
import { map, catchError, take } from 'rxjs/operators';
import { BIC11, BIC8 } from 'src/app/core/constants/validation-regexes';

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

  entityPatternByServiceValidator(service: AbstractControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const pattern = service.value === 'GPL' ? BIC11 : BIC8;
      const error = service.value === 'GPL' ? 'patternBIC11' : 'patternBIC8';
      const match = pattern.test(control.value);
      return match ? null : { [error]: true } ;
    };
  }

  directParticipantValidator(): ValidatorFn {
    return (control: FormGroup): null => {
      const participantType = control.get('entityParticipantType');
      const directParticipant = control.get('directParticipant');

      if (participantType.value === 'INDIRECT' && directParticipant.validator == null){
        directParticipant.setValidators(Validators.required);
        directParticipant.updateValueAndValidity();
      } else if ( participantType.value !== 'INDIRECT' && directParticipant.validator != null) {
        directParticipant.clearValidators();
        directParticipant.updateValueAndValidity();
      }
      return null;
    };
  }

}
