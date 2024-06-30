package com.example.userservice.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@Getter
public class ApplicationConfig {
    @Value("${tempUser.lifespan:12}")
    private int tempUserLifespanInHour;

    @Value("${message.email.from}")
    private String senderEmail;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create ModelMapper
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Encode password
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    /**
//     * filter jwt
//     * @return
//     */
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }

    /**
     *  Create and config  SpringTemplateEngine (implementation of ITemplateEngine in Thymeleaf)
     *  It's used to handle template Thymeleaf
     *  messageSource is message source for templates
     * @return
     */
    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }

    /**
     *  Create and config ClassLoaderTemplateResolver (an implementation of ITemplateResolver in Thymeleaf)
     *  It's used to identify template Thymeleaf and provide configs like prefix, suffix, ... of template files, ...
     * @return
     */
    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * Swagger config
     * http://localhost:8080/api/swagger-ui/index.html
     * @return
     */
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("Api")
                .packagesToScan("com.example.shop.controller") // Specify the package to scan
                .build();
    }
}
