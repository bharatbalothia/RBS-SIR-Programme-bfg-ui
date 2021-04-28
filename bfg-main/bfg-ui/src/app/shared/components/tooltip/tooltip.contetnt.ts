import { TooltipContent } from './tooltip.model';

export const TOOLTIP_CONTENT: TooltipContent = {
    'entity.gpl-entity.entity.create': 'This is the name of the Entity. Usually corresponds to the service agreement entry',
    'entity.gpl-entity.service.create': 'This is the RBS service. For B2B use GPL',
    'entity.gpl-entity.routeInbound.create': 'Check only if the customer will be sending files to RBS',
    'entity.gpl-entity.inboundRequestorDN.create': 'The SWIFT Sender - Customer',
    'entity.gpl-entity.inboundResponderDN.create': 'The SWIFT Receiver - one of the RBS SWIFT DN\'s',
    'entity.gpl-entity.inboundService.create': 'The SWIFT Service',
    'entity.gpl-entity.inboundRequestType.create': 'The file type. Contracts are set up with the customers to indicate the types of files that the customer can send',
    'entity.gpl-swift.requestorDN.create': 'Outbound SWIFT Sender (usually RBS, NWB or Ulster DN)',
    'entity.gpl-swift.responderDN.create': 'Customer\'s SWIFT DN',
    'entity.gpl-swift.service.create': 'SWIFT Service to use for the outbound.',
    'entity.gpl-swift.requestType.create': 'Optional. Message types that may be sent to the customer. Usually blank.',
    'entity.gpl-swift.trace.create': 'SWIFT parameter Trace. Should be no unless otherwise instructed.',
    'entity.gpl-swift.snF.create': 'SWIFT parameter Store and Forward. No will imply Realtime. Customer defines',
    'entity.gpl-swift.deliveryNotification.create': 'SWIFT parameter Delivery notification. Indicates we will get a message back from SWIFT when the file is delivered to the customer. Normally No.',
    'entity.gpl-swift.nonRepudiation.create': 'SWIFT parameter Non-prepudiation. All files delivered will be acknowledged by SWIFT. Normally no.',
    'entity.gpl-swift.e2eSigning.create': 'SWIFT parameter Signing. This is normally Crypto.',
    'entity.gpl-swift.deliveryNotifDN.create': 'SWIFT parameter Delivery Notification DN. Only used if Delivery Notification is checked.',
    'entity.gpl-swift.deliveryNotifRT.create': 'SWIFT parameter Real Time Notification service. Only used if Delivery Notification is checked.',
    'entity.gpl-swift.requestRef.create': 'SWIFT parameter Request Reference. Usually blank.',
    'entity.gpl-swift.fileInfo.create': 'SWIFT parameter File Info. Usually blank.',
    'entity.gpl-swift.fileDesc.create': 'SWIFT parameter File Description. Usually blank.',
    'entity.gpl-swift.transferInfo.create': 'SWIFT parameter Tranfsfer Info. Usually blank.',
    'entity.gpl-swift.transferDesc.create': 'SWIFT parameter Transfer Description. Usually blank.',
    'entity.gpl-confirm.changerComments.create': 'Any comments you want to add. Not used in processing.',
    'entity.gpl-entity.entity.edit': 'This is the name of the Entity. Usually corresponds to the service agreement entry',
    'entity.gpl-entity.service.edit': 'This is the RBS service. For B2B use GPL',
    'entity.gpl-entity.routeInbound.edit': 'Check only if the customer will be sending files to RBS',
    'entity.gpl-entity.inboundRequestorDN.edit': 'The SWIFT Sender - Customer',
    'entity.gpl-entity.inboundResponderDN.edit': 'The SWIFT Receiver - one of the RBS SWIFT DN\'s',
    'entity.gpl-entity.inboundService.edit': 'The SWIFT Service',
    'entity.gpl-entity.inboundRequestType.edit': 'The file type. Contracts are set up with the customers to indicate the types of files that the customer can send',
    'entity.gpl-swift.requestorDN.edit': 'Outbound SWIFT Sender (usually RBS, NWB or Ulster DN)',
    'entity.gpl-swift.responderDN.edit': 'Customer\'s SWIFT DN',
    'entity.gpl-swift.service.edit': 'SWIFT Service to use for the outbound.',
    'entity.gpl-swift.requestType.edit': 'Optional. Message types that may be sent to the customer. Usually blank.',
    'entity.gpl-swift.trace.edit': 'SWIFT parameter Trace. Should be no unless otherwise instructed.',
    'entity.gpl-swift.snF.edit': 'SWIFT parameter Store and Forward. No will imply Realtime. Customer defines',
    'entity.gpl-swift.deliveryNotification.edit': 'SWIFT parameter Delivery notification. Indicates we will get a message back from SWIFT when the file is delivered to the customer. Normally No.',
    'entity.gpl-swift.nonRepudiation.edit': 'SWIFT parameter Non-prepudiation. All files delivered will be acknowledged by SWIFT. Normally no.',
    'entity.gpl-swift.e2eSigning.edit': 'SWIFT parameter Signing. This is normally Crypto.',
    'entity.gpl-swift.deliveryNotifDN.edit': 'SWIFT parameter Delivery Notification DN. Only used if Delivery Notification is checked.',
    'entity.gpl-swift.deliveryNotifRT.edit': 'SWIFT parameter Real Time Notification service. Only used if Delivery Notification is checked.',
    'entity.gpl-swift.requestRef.edit': 'SWIFT parameter Request Reference. Usually blank.',
    'entity.gpl-swift.fileInfo.edit': 'SWIFT parameter File Info. Usually blank.',
    'entity.gpl-swift.fileDesc.edit': 'SWIFT parameter File Description. Usually blank.',
    'entity.gpl-swift.transferInfo.edit': 'SWIFT parameter Tranfsfer Info. Usually blank.',
    'entity.gpl-swift.transferDesc.edit': 'SWIFT parameter Transfer Description. Usually blank.',
    'entity.gpl-confirm.changerComments.edit': 'Any comments you want to add. Not used in processing.',
    'entity.sct-entity.entity.create': 'This is the name of the Entity. Usually corresponds to the service agreement entry',
    'entity.sct-entity.service.create': 'This is the RBS service. For B2B use GPL',
    'entity.sct-entity.maxBulksPerFile.create': 'The maximum number of transactions of a specific type per file. If this number is exceeded then the system will generate more files to send the rest of the transactions.',
    'entity.sct-entity.maxTransfersPerBulk.create': 'The maximum number of transactions per file. If this number is exceeded then the system will generate more files to send all the transactions.',
    'entity.sct-entity.startOfDay.create': 'The hour when processing starts. Outbound transactions received before the start time are bundled in the first bulking of the day.',
    'entity.sct-entity.endOfDay.create': 'The hour when the processing ends. Transactions received after this time will be bundled in the first bundle of the following bulking run.',
    'entity.sct-entity.mailboxPathIn.create': 'The mailbox to receive the inbound files. This is usually /SCT/<BIC>/Inbound',
    'entity.sct-entity.mailboxPathOut.create': 'The mailbox to receive the outbound files. This is usually /SCT/<BIC>/Outbound',
    'entity.sct-entity.mqQueueIn.create': 'The MQ PUMA Queue for the SCT entity to send the unbulked messages to PUMA.',
    'entity.sct-entity.mqQueueOut.create': 'The MQ PUMA Queue for the SCT entity where we receive the unbulked messages from PUMA.',
    'entity.sct-entity.compression.create': 'Indicates if compression of the outbound files is required.Usually false',
    'entity.sct-entity.entityParticipantType.create': 'Indicates if the files is sent on behalf of a Direct or an Indirect Participant of the SEPA Credit Transfer Scheme. Only RBS and NWB are direct participants.',
    'entity.sct-entity.directParticipant.create': 'If Indirect participant was selected then this field indicates the Direct SCT participant',
    'entity.sct-schedule.isWindow.create': 'Window or Daily. ',
    'entity.sct-schedule.timeStart.create': 'If window is used then start time and end time must be specified. If Daily then only start time is specified',
    'entity.sct-schedule.windowEnd.create': 'Used if type is Window to indicate the end of the window.',
    'entity.sct-schedule.windowInterval.create': 'If Window is used it is the time windowInterval to start the bulking process.',
    'entity.sct-schedule.fileType.create': 'Currently this is limited to ICF and QCF files',
    'entity.sct-mqdetails.mqHost.create': 'The Puma MQ Host name',
    'entity.sct-mqdetails.mqPort.create': 'The Puma MQ port number',
    'entity.sct-mqdetails.mqQManager.create': 'The Puma MQ Queue Manager',
    'entity.sct-mqdetails.mqChannel.create': 'The Puma MQ Channel name',
    'entity.sct-mqdetails.mqQueueName.create': 'The Puma MQ Queue name',
    'entity.sct-mqdetails.mqQueueBinding.create': 'The Puma MQ  binding mode. Normally ASQDEF',
    'entity.sct-mqdetails.mqQueueContext.create': 'The Puma MQ  context. Normally NONE',
    'entity.sct-mqdetails.mqDebug.create': 'The Puma MQ Debug.Normally No',
    'entity.sct-mqdetails.mqSSLOptions.create': 'The Puma MQ Encryption. Normally SSL_MUST',
    'entity.sct-mqdetails.mqSSLCiphers.create': 'The Puma MQ Cipher. The MQ team will specify the allowed ciphers.',
    'entity.sct-mqdetails.mqSSLKeyCert.create': 'The Puma MQ Certificate that identities the Sterling Integrator MQ client. Update this if our certificate is renewed/updated.',
    'entity.sct-mqdetails.mqSSLCaCert.create': 'The Puma MQ the CA Root certificate if the CA that signs the Queue Manager certificate. Change this if the remote system updates their signing Authority.',
    'entity.sct-mqdetails.mqHeader.create': 'The header included in the MQ message. PUMA will define this.',
    'entity.sct-mqdetails.mqSessionTimeout.create': 'The Puma MQ timeout.',
    'entity.sct-swift.requestorDN.create': 'The RBS Group swift dn for SEPA Credit Transfers.',
    'entity.sct-swift.responderDN.create': 'The EBA SWIFT dn. ',
    'entity.sct-swift.service.create': 'The SWIFT service from SCT. eba.step2!pu1',
    'entity.sct-swift.requestType.create': 'The SWIFT request type. ',
    'entity.sct-swift.trace.create': 'The SWIFT Trace parameter',
    'entity.sct-swift.snF.create': 'The SWIFT Store and Forward parameter. Unless indicated this should be No as STEP2 is real time',
    'entity.sct-swift.deliveryNotification.create': 'The SWIFT Delivery Notification flag. Usually no',
    'entity.sct-swift.nonRepudiation.create': 'The SWIFT nonRepudiation flag. Usually no',
    'entity.sct-swift.e2eSigning.create': 'The SWIFT End to End Signing. Usually Crypto',
    'entity.sct-swift.deliveryNotifDN.create': 'The SWIFT DN to send Delivery notifications to. Usually not used for SCT',
    'entity.sct-swift.deliveryNotifRT.create': 'The SWIFT message queue to send delivery notifications to. Not used for SCT',
    'entity.sct-swift.requestRef.create': 'The SWIFT Request Reference. Usually blank',
    'entity.sct-swift.fileInfo.create': 'The SWIFT File Info. Usually blank',
    'entity.sct-swift.fileDesc.create': 'The SWIFT File Description. Usually blank',
    'entity.sct-swift.transferInfo.create': 'The SWIFT Transfer Info. Usually blank',
    'entity.sct-swift.transferDesc.create': 'The SWIFT Transfer Description. Usually blank',
    'entity.sct-confirm.changerComments.create': 'Free text field. Not used in processing',
    'entity.sct-entity.entity.edit': 'This is the name of the Entity. Usually corresponds to the service agreement entry',
    'entity.sct-entity.service.edit': 'This is the RBS service. For B2B use GPL',
    'entity.sct-entity.maxBulksPerFile.edit': 'The maximum number of transactions of a specific type per file. If this number is exceeded then the system will generate more files to send the rest of the transactions.',
    'entity.sct-entity.maxTransfersPerBulk.edit': 'The maximum number of transactions per file. If this number is exceeded then the system will generate more files to send all the transactions.',
    'entity.sct-entity.startOfDay.edit': 'The hour when processing starts. Outbound transactions received before the start time are bundled in the first bulking of the day.',
    'entity.sct-entity.endOfDay.edit': 'The hour when the processing ends. Transactions received after this time will be bundled in the first bundle of the following bulking run.',
    'entity.sct-entity.mailboxPathIn.edit': 'The mailbox to receive the inbound files. This is usually /SCT/<BIC>/Inbound',
    'entity.sct-entity.mailboxPathOut.edit': 'The mailbox to receive the outbound files. This is usually /SCT/<BIC>/Outbound',
    'entity.sct-entity.mqQueueIn.edit': 'The MQ PUMA Queue for the SCT entity to send the unbulked messages to PUMA.',
    'entity.sct-entity.mqQueueOut.edit': 'The MQ PUMA Queue for the SCT entity where we receive the unbulked messages from PUMA.',
    'entity.sct-entity.compression.edit': 'Indicates if compression of the outbound files is required.Usually false',
    'entity.sct-entity.entityParticipantType.edit': 'Indicates if the files is sent on behalf of a Direct or an Indirect Participant of the SEPA Credit Transfer Scheme. Only RBS and NWB are direct participants.',
    'entity.sct-entity.directParticipant.edit': 'If Indirect participant was selected then this field indicates the Direct SCT participant',
    'entity.sct-schedule.isWindow.edit': 'Window or Daily. ',
    'entity.sct-schedule.timeStart.edit': 'If window is used then start time and end time must be specified. If Daily then only start time is specified',
    'entity.sct-schedule.windowEnd.edit': 'Used if type is Window to indicate the end of the window.',
    'entity.sct-schedule.windowInterval.edit': 'If Window is used it is the time windowInterval to start the bulking process.',
    'entity.sct-schedule.fileType.edit': 'Currently this is limited to ICF and QCF files',
    'entity.sct-mqdetails.mqHost.edit': 'The Puma MQ Host name',
    'entity.sct-mqdetails.mqPort.edit': 'The Puma MQ port number',
    'entity.sct-mqdetails.mqQManager.edit': 'The Puma MQ Queue Manager',
    'entity.sct-mqdetails.mqChannel.edit': 'The Puma MQ Channel name',
    'entity.sct-mqdetails.mqQueueName.edit': 'The Puma MQ Queue name',
    'entity.sct-mqdetails.mqQueueBinding.edit': 'The Puma MQ  binding mode. Normally ASQDEF',
    'entity.sct-mqdetails.mqQueueContext.edit': 'The Puma MQ  context. Normally NONE',
    'entity.sct-mqdetails.mqDebug.edit': 'The Puma MQ Debug.Normally No',
    'entity.sct-mqdetails.mqSSLOptions.edit': 'The Puma MQ Encryption. Normally SSL_MUST',
    'entity.sct-mqdetails.mqSSLCiphers.edit': 'The Puma MQ Cipher. The MQ team will specify the allowed ciphers.',
    'entity.sct-mqdetails.mqSSLKeyCert.edit': 'The Puma MQ Certificate that identities the Sterling Integrator MQ client. Update this if our certificate is renewed/updated.',
    'entity.sct-mqdetails.mqSSLCaCert.edit': 'The Puma MQ the CA Root certificate if the CA that signs the Queue Manager certificate. Change this if the remote system updates their signing Authority.',
    'entity.sct-mqdetails.mqHeader.edit': 'The header included in the MQ message. PUMA will define this.',
    'entity.sct-mqdetails.mqSessionTimeout.edit': 'The Puma MQ timeout.',
    'entity.sct-swift.requestorDN.edit': 'The RBS Group swift dn for SEPA Credit Transfers.',
    'entity.sct-swift.responderDN.edit': 'The EBA SWIFT dn. ',
    'entity.sct-swift.service.edit': 'The SWIFT service from SCT. eba.step2!pu1',
    'entity.sct-swift.requestType.edit': 'The SWIFT request type. ',
    'entity.sct-swift.trace.edit': 'The SWIFT Trace parameter',
    'entity.sct-swift.snF.edit': 'The SWIFT Store and Forward parameter. Unless indicated this should be No as STEP2 is real time',
    'entity.sct-swift.deliveryNotification.edit': 'The SWIFT Delivery Notification flag. Usually no',
    'entity.sct-swift.nonRepudiation.edit': 'The SWIFT nonRepudiation flag. Usually no',
    'entity.sct-swift.e2eSigning.edit': 'The SWIFT End to End Signing. Usually Crypto',
    'entity.sct-swift.deliveryNotifDN.edit': 'The SWIFT DN to send Delivery notifications to. Usually not used for SCT',
    'entity.sct-swift.deliveryNotifRT.edit': 'The SWIFT message queue to send delivery notifications to. Not used for SCT',
    'entity.sct-swift.requestRef.edit': 'The SWIFT Request Reference. Usually blank',
    'entity.sct-swift.fileInfo.edit': 'The SWIFT File Info. Usually blank',
    'entity.sct-swift.fileDesc.edit': 'The SWIFT File Description. Usually blank',
    'entity.sct-swift.transferInfo.edit': 'The SWIFT Transfer Info. Usually blank',
    'entity.sct-swift.transferDesc.edit': 'The SWIFT Transfer Description. Usually blank',
    'entity.sct-confirm.changerComments.edit': 'Free text field. Not used in processing',
    'trusted-cert.search.by-cert-name.search': 'Enter the trusted certificate name to search for',
    'trusted-cert.search.by-thumbprint.search': 'Enter the trusted certificate thumbprint to search for',
    'trusted-cert.file.file-path.create': 'Select the trusted certificate file to upload',
    'trusted-cert.edit-tc.name.create': 'Enter the trusted certificate name',
    'trusted-cert.edit-tc.changerComments.create': 'Enter comments: free text, not used in processing',
    'trusted-cert.delete-tc.changerComments.delete': 'Enter comments: free text, not used in processing',
    'file-search.search.entity.search': 'Select an entity to find files for',
    'file-search.search.service.search': 'Select a service to find files for',
    'file-search.search.file-status.search': 'Select to find files with a specific status',
    'file-search.search.direction.search': 'Select to find files with a specific direction',
    'file-search.search.bp-state.search': 'Select to find files with a specific business process state',
    'file-search.search.filename.search': 'Select to find files matching a filename',
    'file-search.search.reference.search': 'Select to find files matching a reference',
    'file-search.search.type.search': 'Select to find files with a specific type',
    'file-search.search.from.search': 'Select to find files processed after a particular date/time',
    'file-search.search.to.search': 'Select to find files processed up to a particular date/time',
    'sct-search.search.entity.search': 'Select an entity to find transactions for',
    'sct-search.search.direction.search': 'Select to find transactions with a specific direction',
    'sct-search.search.trans-status.search': 'Select to find transactions with a specific status',
    'sct-search.search.reference.search': 'Select to find transactions matching a reference',
    'sct-search.search.trans-id.search': 'Select to find transactions matching a transaction id',
    'sct-search.search.payment-bic.search': 'Select to find transactions matching a payment BIC',
    'sct-search.search.type.search': 'Select to find transactions with a specific type',
    'sct-search.search.settlement-from.search': 'Select to find transaction settled after a particular date/time',
    'sct-search.search.settlement-to.search': 'Select to find transaction settled up to a particular date/time',
    'sct-search.search.ptimestamp-from.search': 'Select to find transaction with timestamp after a particular date/time',
    'sct-search.search.ptimestamp-to.search': 'Select to find transaction with timestamp to a particular date/time',
};
