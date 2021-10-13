import { FormGroup, ValidatorFn } from '@angular/forms';
import { get } from 'lodash';

export function dateRangeValidator(fromPath: string, endDatePath: string): ValidatorFn {
  return (formGroup: FormGroup) => {
    const from = get(formGroup, fromPath, null);
    const endDate = get(formGroup, endDatePath, null);
    return from && endDate && endDate.isBefore(from) ? { to: true } : null;
  };
}
