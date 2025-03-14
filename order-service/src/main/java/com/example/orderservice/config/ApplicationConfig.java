package com.example.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@Getter
public class ApplicationConfig {
    @Value("${message.email.from}")
    private String senderEmail;

    @Autowired
    private MessageSource messageSource;

    /**
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Create and config  SpringTemplateEngine (implementation of ITemplateEngine in Thymeleaf)
     * It's used to handle template Thymeleaf
     * messageSource is message source for templates
     *
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
     * Create and config ClassLoaderTemplateResolver (an implementation of ITemplateResolver in Thymeleaf)
     * It's used to identify template Thymeleaf and provide configs like prefix, suffix, ... of template files, ...
     *
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
}
