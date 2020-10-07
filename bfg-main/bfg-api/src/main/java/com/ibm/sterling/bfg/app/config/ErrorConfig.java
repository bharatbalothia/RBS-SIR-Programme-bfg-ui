package com.ibm.sterling.bfg.app.config;

import com.ibm.sterling.bfg.app.model.exception.ErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@PropertySource({"classpath:response.properties", "classpath:entityresponse.properties", "classpath:authresponse.properties",
        "classpath:certificateresponse.properties", "classpath:fileresponse.properties"})
public class ErrorConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ErrorMessage getErrorMessage(ErrorCode code) {
        return new ErrorMessage(environment, code);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ErrorMessage getErrorMessage(ErrorCode code, String message, List<Object> data) {
        return new ErrorMessage(environment, code, message, data);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ErrorMessage getErrorMessage(ErrorCode code, List<Object> data) {
        return new ErrorMessage(environment, code, data);
    }

}
