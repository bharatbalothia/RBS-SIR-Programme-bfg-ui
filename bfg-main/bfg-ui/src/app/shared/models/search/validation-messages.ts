export const SEARCH_VALIDATION_MESSAGES = {
  minDateMoreThanMaxDate: 'From date should not be more than to date',
  maxDateLessThanMinDate: 'To date should not be less than from date',
  incorrect: 'Incorrect datetime'
};

export const getSearchValidationMessage = (key: string) => SEARCH_VALIDATION_MESSAGES[key];
