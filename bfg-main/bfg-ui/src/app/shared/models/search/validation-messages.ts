export const SEARCH_VALIDATION_MESSAGES = {
  dateRange: 'To date should not be less than from date',
  incorrect: 'Incorrect datetime'
};

export const getSearchValidationMessage = (key: string) => SEARCH_VALIDATION_MESSAGES[key];
