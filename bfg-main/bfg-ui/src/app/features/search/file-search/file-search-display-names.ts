export const FILE_SEARCH_DISPLAY_NAMES = {
    entity: 'Entity',
    service: 'Service',
    direction: 'Direction',
    fileStatus: 'File Status',
    bpState: 'BP State',
    fileName: 'Filename',
    reference: 'Reference',
    type: 'Type',
    from: 'From',
    to: 'To'
};

export const getFileSearchDisplayName = (key: string) => FILE_SEARCH_DISPLAY_NAMES[key] || key;
