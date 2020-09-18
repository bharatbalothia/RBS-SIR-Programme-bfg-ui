export const SWIFT_DN = /^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\s]*o=swift$/;
export const TIME_24 = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
export const NON_NEGATIVE_INT = /^\d+$/;
export const BIC11 = /^([a-zA-Z]){4}([a-zA-Z]){2}([0-9a-zA-Z]){2}([0-9a-zA-Z]{3})$/;
export const BIC8 = /^[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]$/;
export const TRUSTED_CERTIFICATE_NAME = /^[0-9a-zA-Z _\-:.]+$/;
