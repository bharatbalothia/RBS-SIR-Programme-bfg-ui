package com.ibm.sterling.bfg.app.exception;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;

@Configuration
public class RestTemplateExceptionHandler {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ResponseEntity handleRestTemplateException(HttpStatusCodeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Optional.ofNullable(message)
                        .map(errMessage -> {
                                    int errorStartIndex = message.indexOf("[");
                                    int errorEndIndex = message.lastIndexOf("]");
                                    return errorStartIndex == -1 || errorEndIndex == -1 ? errMessage :
                                            errMessage.substring(errorStartIndex, errorEndIndex + 1);
                                }
                        ).orElse(message));
    }

}
