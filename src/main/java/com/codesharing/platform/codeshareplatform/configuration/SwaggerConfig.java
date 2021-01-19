package com.codesharing.platform.codeshareplatform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final Contact CUSTOM_CONTACT = new
            Contact("Ritish", "https://github.com/ritish78",
            "github.com/ritish78");
    /**
     * Api Info constructor
     * String title, String description, String version,
     * String termsOfServiceUrl, Contact contact, String license,
     * String licenseUrl, Collection<VendorExtension> vendorExtensions
     */

    public static final ApiInfo CUSTOM_API_INFO = new ApiInfo("Api Documentation", "This is the documentation for Code Share api. All the HTTP requests and their return type is shown here.",
            "v0.0.1", "no TOS", CUSTOM_CONTACT, "No License",
            "noUrlForLicense", Collections.emptyList());
    private static final Set<String> CUSTOM_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList("application/json", "application/xml"));


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(CUSTOM_API_INFO)
                .produces(CUSTOM_PRODUCES_AND_CONSUMES)
                .consumes(CUSTOM_PRODUCES_AND_CONSUMES);

    }
}
