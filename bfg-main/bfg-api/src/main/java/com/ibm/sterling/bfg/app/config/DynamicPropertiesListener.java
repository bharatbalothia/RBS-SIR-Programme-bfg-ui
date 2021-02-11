package com.ibm.sterling.bfg.app.config;

import com.ibm.websphere.security.auth.data.AuthData;
import com.ibm.websphere.security.auth.data.AuthDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.security.auth.login.LoginException;
import java.util.Properties;

import static com.ibm.websphere.crypto.PasswordUtil.passwordDecode;

public class DynamicPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static final Logger LOG = LogManager.getLogger(DynamicPropertiesListener.class);

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        try {
            AuthData jdbcAuthData = AuthDataProvider.getAuthData("jdbcAuthData");
            AuthData apiAuthData = AuthDataProvider.getAuthData("apiAuthData");
            AuthData tokenSecretAuthData = AuthDataProvider.getAuthData("tokenSecretAuthData");
            Properties props = new Properties();
            props.put("spring.datasource.username", passwordDecode(jdbcAuthData.getUserName()));
            props.put("spring.datasource.password", passwordDecode(new String(jdbcAuthData.getPassword())));
            props.put("api.userName", passwordDecode(apiAuthData.getUserName()));
            props.put("api.password", passwordDecode(new String(apiAuthData.getPassword())));
            props.put("security.jwt.tokenSigningKey", passwordDecode(new String(tokenSecretAuthData.getPassword())));
            environment.getPropertySources().addLast(new PropertiesPropertySource("dynamicProperties", props));
        } catch (LoginException e) {
            LOG.error("LoginException in onApplicationEvent of DynamicPropertiesListener: {}", e.getMessage());
        }
    }

}
