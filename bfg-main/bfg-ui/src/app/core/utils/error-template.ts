import { ERROR_MESSAGES } from '../constants/error-messages';

export interface ErrorsField {
    [key: string]: string;
}

export interface ErrorMessage {
    code: string;
    message: string;
    errors?: ErrorsField[];
}

export const getApiErrorMessage = (error) => {
    if (error.error && error.error.message && error.error.code) {
        error.error.message = ERROR_MESSAGES[error.error.message] || error.error.message;
        return error.error;
    } else {
        return error;
    }
};