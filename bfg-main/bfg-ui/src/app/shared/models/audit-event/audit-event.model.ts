export interface AuditEvent {
    action: string;
    actionType: string;
    actor: string;
    created: string;
    eventContext: string;
    eventType: string;
    id: string;
    objectActedOn: string;
}
