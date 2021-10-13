export const FILES_WITH_VALUE_DISPLAY_NAMES = {
    from: 'From',
    to: 'To',
};

export const getFilesWithValueDisplayName = (key: string) => FILES_WITH_VALUE_DISPLAY_NAMES[key] || key;
