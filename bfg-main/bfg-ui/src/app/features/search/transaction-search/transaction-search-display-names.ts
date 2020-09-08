export const TRANSACTION_SEARCH_DISPLAY_NAMES = {
    entity: 'Entity',
    service: 'Service',
    direction: 'Direction',
    trxStatus: 'Transaction Status',
    reference: 'Reference',
    transactionID: 'Transaction ID',
    paymentBIC: 'Payment BIC',
    type: 'Type',
    from: 'From',
    to: 'To',
};

export const getTransactionSearchDisplayName = (key: string) => TRANSACTION_SEARCH_DISPLAY_NAMES[key] || key;
