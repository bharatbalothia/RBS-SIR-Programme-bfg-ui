package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;

@Component
public class ExceptionDetailsHandler {

    public ResponseEntity handleRestTemplateException(HttpStatusCodeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Optional.ofNullable(message)
                        .map(errorMessage -> {
                                    int errorStartIndex = errorMessage.indexOf("[");
                                    int errorEndIndex = errorMessage.lastIndexOf("]");
                                    return errorStartIndex == -1 || errorEndIndex == -1 ? errorMessage :
                                            errorMessage.substring(errorStartIndex, errorEndIndex + 1);
                                }
                        ).orElse(message));
    }

}
