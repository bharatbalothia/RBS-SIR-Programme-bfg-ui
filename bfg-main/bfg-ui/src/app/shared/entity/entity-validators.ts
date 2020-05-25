import { Injectable } from '@angular/core';
import { AbstractControl, ValidationErrors, AsyncValidatorFn, ValidatorFn } from '@angular/forms';
import { EntityService } from './entity.service';
import { Observable, of } from 'rxjs';
import { map, catchError, take } from 'rxjs/operators';

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
      const pattern = service.value === 'GPL' ?
        '^([a-zA-Z]){4}([a-zA-Z]){2}([0-9a-zA-Z]){2}([0-9a-zA-Z]{3})$' :
        '^[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]$';
      const error = service.value === 'GPL' ? 'dontMatchGPL' : 'dontMatchPattern';
      const regexp = new RegExp(pattern);
      const match = regexp.test(control.value);
      return match ? null : { [error]: true } ;
    };
  }

}
