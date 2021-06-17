import { ERROR_MESSAGES } from '../constants/error-messages';
import { get } from 'lodash';

export interface ErrorsField {
    [key: string]: string;
}

export interface ErrorMessage {
    code: string;
    message: string;
    errors?: ErrorsField[];
    warnings?: ErrorsField[];
    htmlContent?: string;
}

export const getApiErrorMessage = (error) => {
    if (error.error && error.error.message && error.error.code) {
        error.error.message = ERROR_MESSAGES[error.error.message] || error.error.message;
        return error.error;
    } else {
        return error;
    }
};

export const getErrorsMessage = (error: ErrorsField) => Object.keys(error).map(e => error[e]);

export const getErrorByField = (key, errorMessage: ErrorMessage) =>
    get(errorMessage, 'errors', []).filter(el => el[key]).map(el => el[key]).join('\n ');
