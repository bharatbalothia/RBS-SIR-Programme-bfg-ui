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
        return error.error;
    } else {
        return error;
    }
}