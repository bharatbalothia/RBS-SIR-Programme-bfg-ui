export const ENTITY_VALIDATION_MESSAGES = {
    service: [
        { type: 'required', message: 'Entity type is required' },
    ],
    entity: [
        { type: 'required', message: 'Entity is required' },
        { type: 'entityExists', message: 'Entuty with this name already exists' },
    ],
    inboundRequestorDN: [
        { type: 'required', message: 'Inbound Requestor DN is required' },
    ],
    inboundResponderDN: [
        { type: 'required', message: 'Inbound Responder DN is required' },
    ],
    inboundService: [
        { type: 'required', message: 'Inbound Service is required' },
    ],
    requestorDN: [
        { type: 'required', message: 'Requestor DN is required' },
    ],
    responderDN: [
        { type: 'required', message: 'Responder DN is required' },
    ]
};
