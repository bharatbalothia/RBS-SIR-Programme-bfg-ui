package com.ibm.sterling.bfg.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@ConditionalOnExpression(value = "${use.swagger}")
@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ibm.sterling"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiDetails())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(AUTHORIZATION, authorizationScopes));
    }

    private ApiInfo apiDetails() {
        return new ApiInfoBuilder()
                .title("BFG UI")
                .description("SBI User Interface from Natwest and IBM")
                .version(buildProperties.getVersion())
                .build();
    }

    @Bean
    public TranslationOperationBuilderPlugin translationPlugin() {
        return new TranslationOperationBuilderPlugin();
    }

    public static class TranslationOperationBuilderPlugin implements OperationBuilderPlugin {

        @Autowired
        protected Environment env;

        @Override
        public boolean supports(DocumentationType delimiter) {
            return true;
        }

        @Override
        public void apply(OperationContext context) {
            Set<ResponseMessage> messages = context.operationBuilder().build().getResponseMessages();

            Set<ResponseMessage> translated = new HashSet<>();
            for (ResponseMessage untranslated : messages) {
                String translation = env.getProperty(untranslated.getMessage());

                translated.add(new ResponseMessage(untranslated.getCode(),
                        translation,
                        untranslated.getResponseModel(),
                        untranslated.getHeaders(),
                        untranslated.getVendorExtensions()
                ));
            }
            context.operationBuilder().responseMessages(translated);
        }
    }
}
