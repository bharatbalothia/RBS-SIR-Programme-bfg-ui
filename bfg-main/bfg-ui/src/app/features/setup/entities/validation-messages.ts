import { getDisplayName } from './display-names';

export const ENTITY_VALIDATION_MESSAGES = {
    service: [
        { type: 'required', message: `${getDisplayName('service')} is required` },
    ],
    entity: [
        { type: 'required', message: `${getDisplayName('entity')} is required` },
        { type: 'entityExists', message: `${getDisplayName('entity')} with this name already exists` },
        { type: 'patternBIC11', message: `${getDisplayName('entity')} should be in BIC11 format` },
        { type: 'patternBIC8', message: `${getDisplayName('entity')} should be in BIC8 format` }
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
    ],
    maxTransfersPerBulk: [
        { type: 'required', message: `${getDisplayName('maxTransfersPerBulk')} is required` },
    ],
    startOfDay: [
        { type: 'required', message: `${getDisplayName('startOfDay')} is required` },
    ],
    endOfDay: [
        { type: 'required', message: `${getDisplayName('endOfDay')} is required` },
    ],
    mailboxPathIn: [
        { type: 'required', message: `${getDisplayName('mailboxPathIn')} is required` },
    ],
    mailboxPathOut: [
        { type: 'required', message: `${getDisplayName('mailboxPathOut')} is required` },
    ],
    directParticipant: [
        { type: 'required', message: `${getDisplayName('directParticipant')} is required if ${getDisplayName('entityParticipantType')} is INDIRECT` },
    ]
};

export const SCHEDULE_VALIDATION_MESSAGES = {
    isWindow: [
        { type: 'required', message: `${getDisplayName('isWindow')} is required` },
    ],
    timeStart: [
        { type: 'required', message: `${getDisplayName('timeStart')} is required` },
    ],
    windowEnd: [
        { type: 'required', message: `${getDisplayName('windowEnd')} is required` },
    ],
    windowInterval: [
        { type: 'required', message: `${getDisplayName('windowInterval')} is required` },
    ],
    fileType: [
        { type: 'required', message: `${getDisplayName('fileType')} is required` },
    ]
};
