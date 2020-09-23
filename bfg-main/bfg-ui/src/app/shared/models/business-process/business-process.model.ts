export interface BusinessProcess {
    businessProcess: string;
    commitStepsUponError: string;
    deadlineHours: string;
    deadlineMinutes: string;
    description: string;
    documentStorage: string;
    documentTracking: string;
    enableTransaction: string;
    eventReportingLevel: string;
    expedite: string;
    firstNotificationHours: string;
    firstNotificationMinutes: string;
    lifespanDays: string;
    lifespanHours: string;
    lifespanType: string;
    name: string;
    onfaultProcessing: string;
    persistenceLevel: string;
    queue: string;
    recoveryLevel: string;
    removalMethod: string;
    secondNotificationHours: string;
    secondNotificationMinutes: string;
    softstopRecoveryLevel: string;
    startMode: string;
    wfdVersion: string;
}
