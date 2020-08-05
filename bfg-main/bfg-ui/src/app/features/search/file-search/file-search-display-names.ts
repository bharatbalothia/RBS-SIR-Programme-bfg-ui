export const FILE_SEARCH_DISPLAY_NAMES = {
    fileStatus: 'File Status',
    type: 'Type'
};

export const getFileSearchDisplayName = (key: string) => FILE_SEARCH_DISPLAY_NAMES[key] || key;
