export interface ErrorsField {
    [key: string]: string;
}

export interface ErrorMessage {
    code: string;
    message: string;
    errors?: ErrorsField[];
}
