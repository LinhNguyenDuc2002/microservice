package com.example.userservice.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class I18nServiceImpl implements I18nService {
    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public String getMessage(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }
}
