import { Injectable } from '@angular/core';
import { AsyncValidator, AbstractControl, ValidationErrors, AsyncValidatorFn, FormControl } from '@angular/forms';
import { EntityService } from './entity.service';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EntityValidators{

  constructor(private entityService: EntityService) { }

  entityExistsValidator(service: AbstractControl): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.entityService.isEntityExists(service.value, control.value).pipe(
        map(exists => {
          return exists ? { entityExists: true } : null;
        })
      );
    };
  }

}
