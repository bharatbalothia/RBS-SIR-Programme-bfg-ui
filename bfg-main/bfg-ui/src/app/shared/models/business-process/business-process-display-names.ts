export const BUSINESS_PROCESS_DISPLAY_NAMES = {
    advStatus: 'Advanced Status',
    docId: 'Document',
    endTime: 'Ended',
    exeState: 'Status',
    nodeExecuted: 'Execution Node',
    serviceName: 'Service',
    startTime: 'Started',
    statusRpt: 'Status Report',
    stepId: 'Step'
};

export const getBusinessProcessDisplayName = (key: string) => BUSINESS_PROCESS_DISPLAY_NAMES[key] || key;
