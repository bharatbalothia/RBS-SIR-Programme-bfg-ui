package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.validation.Field;

public enum DetailsField implements Field {
    ENTITY_PARTICIPANT_TYPE("entityParticipantType"),
    DIRECT_PARTICIPANT("directParticipant"),
    MQ_PORT("mqPort"),
    MQ_HOST("mqHost"),
    MQ_QMANAGER("mqQManager"),
    MQ_CHANNEL("mqChannel"),
    MQ_QNAME("mqQueueName"),
    MQ_QBINDING("mqQueueBinding"),
    MQ_QCONTEXT("mqQueueContext"),
    MQ_DEBUG("mqDebug"),
    MQ_SSLOPTION("mqSSLOptions"),
    MQ_SSLCIPHERS("mqSSLCiphers"),
    MQ_SSLSYSTEMCERTID("mqSSLKeyCert"),
    MQ_SSLCACERTID("mqSSLCaCert"),
    MQ_HEADER("mqHeader"),
    MQ_SESSIONTIMEOUT("mqSessionTimeout"),
    REQUESTORDN("requestorDN"),
    RESPONDERDN("responderDN"),
    SERVICENAME("serviceName"),
    REQUESTTYPE("requestType");

    DetailsField(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
