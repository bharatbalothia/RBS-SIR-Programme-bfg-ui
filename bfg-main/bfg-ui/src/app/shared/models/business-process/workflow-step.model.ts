export interface WorkflowStep {
    advStatus: string;
    docId: string;
    endTime: string;
    exeState: string;
    nodeExecuted: string;
    serviceName: string;
    startTime: string;
    statusRpt: string;
    wfcId: string;
    wfdId: number;
    wfdVersion: number;
    stepId: number;
    inlineInvocation: boolean;
}
