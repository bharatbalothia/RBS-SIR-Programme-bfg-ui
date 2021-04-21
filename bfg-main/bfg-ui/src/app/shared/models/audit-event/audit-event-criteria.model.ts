export interface AuditEventCriteria {
    action?: string;
    actionType?: string;
    eventType?: string[];
    id?: string;
    objectActedOn?: string;
    size: number;
    username?: string;
}
