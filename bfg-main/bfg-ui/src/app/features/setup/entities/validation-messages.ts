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
        { type: 'pattern', message: 'Inbound Requestor DN should be [ou={ou}]o={BIC8},o=swift format' }
    ],
    inboundResponderDN: [
        { type: 'required', message: 'Inbound Responder DN is required' },
        { type: 'pattern', message: 'Inbound Responder DN should be [ou={ou}]o={BIC8},o=swift format' }
    ],
    inboundService: [
        { type: 'required', message: 'Inbound Service is required' },
    ],
    requestorDN: [
        { type: 'required', message: 'Requestor DN is required' },
        { type: 'pattern', message: 'Requestor DN  should be [ou={ou}]o={BIC8},o=swift format' }
    ],
    responderDN: [
        { type: 'required', message: 'Responder DN is required' },
        { type: 'pattern', message: 'Responder DN should be [ou={ou}]o={BIC8},o=swift format' }
    ]
};

export const SCHEDULE_VALIDATION_MESSAGES = {
    isWindow: [
        { type: 'required', message: 'Schedule type is required' },
    ],
    timeStart: [
        { type: 'required', message: 'Time Start is required' },
    ],
    windowEnd: [
        { type: 'required', message: 'Time End is required' },
    ],
    windowInterval: [
        { type: 'required', message: 'Time Interval is required' },
    ],
};
