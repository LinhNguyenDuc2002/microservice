package com.example.servicefoundation.i18n;

import java.util.Locale;

public interface I18nService {
    String getMessage(String key, Locale locale);

    String getMessage(String key, Object[] args, Locale locale);
}
