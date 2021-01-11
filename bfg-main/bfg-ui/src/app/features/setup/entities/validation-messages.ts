import { getEntityDisplayName } from './entity-display-names';

export const ENTITY_VALIDATION_MESSAGES = {
    service: [
        { type: 'required', message: `${getEntityDisplayName('service')} is required` },
        { type: 'forbidden', message: `You don't have enough permissions to proceed with this ${getEntityDisplayName('service')}` }
    ],
    entity: [
        { type: 'required', message: `${getEntityDisplayName('entity')} is required` },
        { type: 'entityExists', message: `${getEntityDisplayName('entity')} with this name already exists` },
        { type: 'patternBIC11', message: `${getEntityDisplayName('entity')} must be a valid BIC11 format` },
        { type: 'patternBIC8', message: `${getEntityDisplayName('entity')} must be a valid BIC8 format` }
    ],
    inboundRequestorDN: [
        { type: 'required', message: `${getEntityDisplayName('inboundRequestorDN')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('inboundRequestorDN')} should be [ou={ou}]o={BIC8},o=swift format` },
    ],
    inboundResponderDN: [
        { type: 'required', message: `${getEntityDisplayName('inboundResponderDN')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('inboundResponderDN')} should be [ou={ou}]o={BIC8},o=swift format` },
    ],
    inboundService: [
        { type: 'required', message: `${getEntityDisplayName('inboundService')} is required` },
    ],
    inboundRequestType: [
        { type: 'required', message: `${getEntityDisplayName('inboundRequestType')} is required` },
    ],
    requestorDN: [
        { type: 'required', message: `${getEntityDisplayName('requestorDN')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('requestorDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    serviceName: [
        { type: 'required', message: `${getEntityDisplayName('service')} is required` }
    ],
    requestType: [
        { type: 'required', message: `${getEntityDisplayName('requestType')} is required` }
    ],
    responderDN: [
        { type: 'required', message: `${getEntityDisplayName('responderDN')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('responderDN')} should be [ou={ou}]o={BIC8},o=swift format` }
    ],
    maxBulksPerFile: [
        { type: 'required', message: `${getEntityDisplayName('maxBulksPerFile')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('maxBulksPerFile')} must be a positive number` }
    ],
    maxTransfersPerBulk: [
        { type: 'required', message: `${getEntityDisplayName('maxTransfersPerBulk')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('maxTransfersPerBulk')} must be a positive number` }
    ],
    startOfDay: [
        { type: 'required', message: `${getEntityDisplayName('startOfDay')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('startOfDay')} must be in the HH:mm format (24HR)` },
    ],
    endOfDay: [
        { type: 'required', message: `${getEntityDisplayName('endOfDay')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('endOfDay')} must be in the HH:mm format (24HR)` },
    ],
    mailboxPathIn: [
        { type: 'required', message: `${getEntityDisplayName('mailboxPathIn')} is required` },
    ],
    mailboxPathOut: [
        { type: 'required', message: `${getEntityDisplayName('mailboxPathOut')} is required` },
        { type: 'mailboxPathOutExists', message: `${getEntityDisplayName('entity')} with this ${getEntityDisplayName('mailboxPathOutExists')} already exists` },
    ],
    mqQueueOut: [
        { type: 'mqQueueOutExists', message: `${getEntityDisplayName('entity')} with this ${getEntityDisplayName('mqQueueOut')} already exists` },
    ],
    directParticipant: [
        { type: 'required', message: `${getEntityDisplayName('directParticipant')} is required if ${getEntityDisplayName('entityParticipantType')} is INDIRECT` },
    ],
    mqHost: [
        { type: 'required', message: `${getEntityDisplayName('mqHost')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqPort: [
        { type: 'required', message: `${getEntityDisplayName('mqPort')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
        { type: 'pattern', message: `${getEntityDisplayName('mqPort')} must be a positive number` },
    ],
    mqQManager: [
        { type: 'required', message: `${getEntityDisplayName('mqQManager')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqChannel: [
        { type: 'required', message: `${getEntityDisplayName('mqChannel')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqQueueName: [
        { type: 'required', message: `${getEntityDisplayName('mqQueueName')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqHeader: [
        { type: 'required', message: `${getEntityDisplayName('mqHeader')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSessionTimeout: [
        { type: 'required', message: `${getEntityDisplayName('mqSessionTimeout')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
        { type: 'pattern', message: `${getEntityDisplayName('mqSessionTimeout')} must be a positive number` },
    ],
    mqQueueBinding: [
        { type: 'required', message: `${getEntityDisplayName('mqQueueBinding')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqQueueContext: [
        { type: 'required', message: `${getEntityDisplayName('mqQueueContext')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqDebug: [
        { type: 'required', message: `${getEntityDisplayName('mqDebug')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSSLOptions: [
        { type: 'required', message: `${getEntityDisplayName('mqSSLOptions')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSSLCiphers: [
        { type: 'required', message: `${getEntityDisplayName('mqSSLCiphers')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSSLKeyCert: [
        { type: 'required', message: `${getEntityDisplayName('mqSSLKeyCert')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
    mqSSLCaCert: [
        { type: 'required', message: `${getEntityDisplayName('mqSSLCaCert')} is required if ${getEntityDisplayName('entityParticipantType')} is DIRECT` },
    ],
};

export const SCHEDULE_VALIDATION_MESSAGES = {
    isWindow: [
        { type: 'required', message: `${getEntityDisplayName('isWindow')} is required` },
    ],
    timeStart: [
        { type: 'required', message: `${getEntityDisplayName('timeStart')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('timeStart')} must be in the HH:mm format (24HR)` },
    ],
    windowEnd: [
        { type: 'required', message: `${getEntityDisplayName('windowEnd')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('windowEnd')} must be in the HH:mm format (24HR)` },
    ],
    windowInterval: [
        { type: 'required', message: `${getEntityDisplayName('windowInterval')} is required` },
        { type: 'pattern', message: `${getEntityDisplayName('windowInterval')}  must be a positive number` }
    ],
    fileType: [
        { type: 'required', message: `${getEntityDisplayName('fileType')} is required` },
    ]
};
