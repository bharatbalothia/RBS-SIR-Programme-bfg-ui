package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.model.exception.ErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource({"classpath:response.properties", "classpath:entityresponse.properties",
        "classpath:authresponse.properties", "classpath:certificateresponse.properties",
        "classpath:fileresponse.properties", "classpath:eventresponse.properties"})
public class ErrorMessageHandler {

    @Autowired
    private Environment environment;

    public ErrorMessage getErrorMessage(ErrorCode code) {
        return new ErrorMessage(environment, code);
    }

    public ErrorMessage getErrorMessage(ErrorCode code, String message, List<Object> data) {
        return new ErrorMessage(environment, code, message, data);
    }

    public ErrorMessage getErrorMessage(ErrorCode code, List<Object> data) {
        return new ErrorMessage(environment, code, data);
    }

}
