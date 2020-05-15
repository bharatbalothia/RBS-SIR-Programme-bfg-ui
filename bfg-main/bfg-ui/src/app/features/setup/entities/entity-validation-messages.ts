export const ENTITY_VALIDATION_MESSAGES = {
    entityTypeSelect: [
        { type: 'required', message: 'Entity type is required' },
    ],
    entity: [
        { type: 'required', message: 'Entity is required' },
        { type: 'pattern', message: 'Enter valid BIC11 format'}
    ],
    inboundRequestorDN: [
        { type: 'required', message: 'Inbound Requestor DN is required' },
    ]
};
