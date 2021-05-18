export const TRANSACTIONS_WITH_VALUE_DISPLAY_NAMES = {
    from: 'From',
    to: 'To',
};

export const getTransactionsWithValueDisplayName = (key: string) => TRANSACTIONS_WITH_VALUE_DISPLAY_NAMES[key] || key;