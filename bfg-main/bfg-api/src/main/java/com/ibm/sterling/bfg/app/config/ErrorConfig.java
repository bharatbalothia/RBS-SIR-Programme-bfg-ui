package com.ibm.sterling.bfg.app.config;

import com.ibm.sterling.bfg.app.model.exception.ErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import java.util.List;

@Configuration
@PropertySource({"classpath:response.properties", "classpath:entityresponse.properties"})
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
    public ErrorMessage getErrorMessage(ErrorCode code, String message) {
        return new ErrorMessage(environment, code, message);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ErrorMessage getErrorMessage(ErrorCode errorCode, List<Object> data) {
        return new ErrorMessage(environment, errorCode, data);
    }
}
