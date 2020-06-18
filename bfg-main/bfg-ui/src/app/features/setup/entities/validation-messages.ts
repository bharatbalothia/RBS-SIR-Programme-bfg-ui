import { getDisplayName } from './display-names';

export const ENTITY_VALIDATION_MESSAGES = {
    service: [
        { type: 'required', message: `${getDisplayName('service')} is required` },
    ],
    entity: [
        { type: 'required', message: `${getDisplayName('entity')} is required` },
        { type: 'entityExists', message: `${getDisplayName('entity')} with this name already exists` },
        { type: 'patternBIC11', message: `${getDisplayName('entity')} must be a valid BIC11 format` },
        { type: 'patternBIC8', message: `${getDisplayName('entity')} must be a valid BIC8 format` }
    ],
    inboundRequestorDN: [
        { type: 'required', message: `${getDisplayName('inboundRequestorDN')} is required` },
        { type: 'pattern', message: `${getDisplayName('inboundRequestorDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    inboundResponderDN: [
        { type: 'required', message: `${getDisplayName('inboundResponderDN')} is required` },
        { type: 'pattern', message: `${getDisplayName('inboundResponderDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    inboundService: [
        { type: 'required', message: `${getDisplayName('inboundService')} is required` },
    ],
    requestorDN: [
        { type: 'required', message: `${getDisplayName('requestorDN')} is required` },
        { type: 'pattern', message: `${getDisplayName('requestorDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    responderDN: [
        { type: 'required', message: `${getDisplayName('responderDN')} is required` },
        { type: 'pattern', message: `${getDisplayName('responderDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    maxBulksPerFile: [
        { type: 'required', message: `${getDisplayName('maxBulksPerFile')} is required` },
        { type: 'pattern', message: `${getDisplayName('maxBulksPerFile')} must be a positive number` }
    ],
    maxTransfersPerBulk: [
        { type: 'required', message: `${getDisplayName('maxTransfersPerBulk')} is required` },
        { type: 'pattern', message: `${getDisplayName('maxTransfersPerBulk')} must be a positive number` }
    ],
    startOfDay: [
        { type: 'required', message: `${getDisplayName('startOfDay')} is required` },
        { type: 'pattern', message: `${getDisplayName('startOfDay')} must be in the HH:mm format (24HR)` },
    ],
    endOfDay: [
        { type: 'required', message: `${getDisplayName('endOfDay')} is required` },
        { type: 'pattern', message: `${getDisplayName('endOfDay')} must be in the HH:mm format (24HR)` },
    ],
    mailboxPathIn: [
        { type: 'required', message: `${getDisplayName('mailboxPathIn')} is required` },
    ],
    mailboxPathOut: [
        { type: 'required', message: `${getDisplayName('mailboxPathOut')} is required` },
    ],
    directParticipant: [
        { type: 'required', message: `${getDisplayName('directParticipant')} is required if ${getDisplayName('entityParticipantType')} is INDIRECT` },
    ],
    mqHost: [
        { type: 'required', message: `${getDisplayName('mqHost')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqPort: [
        { type: 'required', message: `${getDisplayName('mqPort')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
        { type: 'pattern', message: `${getDisplayName('mqPort')} must be a positive number`},
    ],
    mqQManager: [
        { type: 'required', message: `${getDisplayName('mqQManager')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqChannel: [
        { type: 'required', message: `${getDisplayName('mqChannel')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqQueueName: [
        { type: 'required', message: `${getDisplayName('directParticipant')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqHeader: [
        { type: 'required', message: `${getDisplayName('directParticipant')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSessionTimeout: [
        { type: 'required', message: `${getDisplayName('directParticipant')} is required if ${getDisplayName('entityParticipantType')} is DIRECT` },
        { type: 'pattern', message: `${getDisplayName('directParticipant')} must be a positive number`},
    ]
};

export const SCHEDULE_VALIDATION_MESSAGES = {
    isWindow: [
        { type: 'required', message: `${getDisplayName('isWindow')} is required` },
    ],
    timeStart: [
        { type: 'required', message: `${getDisplayName('timeStart')} is required` },
        { type: 'pattern', message: `${getDisplayName('timeStart')} must be in the HH:mm format (24HR)` },
    ],
    windowEnd: [
        { type: 'required', message: `${getDisplayName('windowEnd')} is required` },
        { type: 'pattern', message: `${getDisplayName('windowEnd')} must be in the HH:mm format (24HR)` },
    ],
    windowInterval: [
        { type: 'required', message: `${getDisplayName('windowInterval')} is required` },
        { type: 'pattern', message: `${getDisplayName('windowInterval')}  must be a positive number` }
    ],
    fileType: [
        { type: 'required', message: `${getDisplayName('fileType')} is required` },
    ]
};
