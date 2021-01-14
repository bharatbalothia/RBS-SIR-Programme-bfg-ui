import { FormGroup, ValidatorFn } from '@angular/forms';
import { get } from 'lodash';

export function dateRangeValidator(startDatePath: string, endDatePath: string): ValidatorFn {
  return (formGroup: FormGroup) => {
    const startDate = get(formGroup, startDatePath, null);
    const endDate = get(formGroup, endDatePath, null);
    return startDate && endDate && endDate.isBefore(startDate) ? { to: true } : null;
  };
}
