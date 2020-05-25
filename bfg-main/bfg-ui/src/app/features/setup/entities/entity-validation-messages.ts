export const ENTITY_VALIDATION_MESSAGES = {
    service: [
        { type: 'required', message: 'Entity type is required' },
    ],
    entity: [
        { type: 'required', message: 'Entity is required' },
        { type: 'entityExists', message: 'Entuty with this name already exists' },
        { type: 'patternBIC11', message: 'Entity should be in BIC11 format' },
        { type: 'patternBIC8', message: 'Entity should be in BIC8 format' }
    ],
    inboundRequestorDN: [
        { type: 'required', message: 'Inbound Requestor DN is required' },
        { type: 'pattern', message: 'Inbound Requestor DN should be [ou={ou}]o={BIC8},o=swift format'}
    ],
    inboundResponderDN: [
        { type: 'required', message: 'Inbound Responder DN is required' },
        { type: 'pattern', message: 'Inbound Responder DN should be [ou={ou}]o={BIC8},o=swift format'}
    ],
    inboundService: [
        { type: 'required', message: 'Inbound Service is required' },
    ],
    requestorDN: [
        { type: 'required', message: 'Requestor DN is required' },
        { type: 'pattern', message: 'Requestor DN  should be [ou={ou}]o={BIC8},o=swift format'}
    ],
    responderDN: [
        { type: 'required', message: 'Responder DN is required' },
        { type: 'pattern', message: 'Responder DN should be [ou={ou}]o={BIC8},o=swift format'}
    ]
};
