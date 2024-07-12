package com.example.orderservice.webclient;

import jakarta.validation.constraints.NotNull;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface WebClientProcessor {
    /**
     * @param uri        The request url
     * @param header     The request header
     * @param queryParam The query param
     * @param <T>        The response class type
     * @return
     */
    <T> T get(@NotNull final String uri,
              final Map<String, String> header,
              final MultiValueMap<String, String> queryParam,
              final Class<T> clazz) throws Exception;

//    /**
//     * @param uri    The request url
//     * @param header The request header
//     * @param body   The request body
//     * @param clazz  The response class type
//     * @param <T>    The response class type
//     * @return
//     */
//    <T> T post(@NotNull final String uri,
//               final Map<String, String> header,
//               final Object body,
//               final Class<T> clazz) throws Exception;

//    /**
//     * @param uri      The request url
//     * @param header   The request header
//     * @param formData The request form data
//     * @param clazz    The response class type
//     * @param <T>      The response class type
//     * @return
//     */
//    <T> T post(@NotNull final String uri,
//               final Map<String, String> header,
//               final MultiValueMap<String, String> formData,
//               final Class<T> clazz);

//    /**
//     * @param uri    The request url
//     * @param header The request header
//     * @param body   The request body
//     * @param clazz  The response class type
//     * @param <T>    The response class type
//     * @return
//     */
//    <T> T put(@NotNull final String uri,
//              final Map<String, String> header,
//              final Object body,
//              final Class<T> clazz) throws Exception;

    /**
     * @param uri    The request url
     * @param header The request header
     * @param body   The request body
     * @param clazz  The response class type
     * @param <T>    The response class type
     * @return
     */
    <T> T patch(@NotNull final String uri,
                final Map<String, String> header,
                final Object body,
                final Class<T> clazz) throws Exception;

//    /**
//     * @param uri    The request url
//     * @param header The request header
//     */
//    void delete(@NotNull final String uri,
//                final Map<String, String> header);
//
//    /**
//     * @param uri    The request url
//     * @param header The request header
//     * @param body   The request body
//     * @param clazz  The response class type
//     * @param <T>    The response class type
//     * @return
//     */
//    <T> T[] delete(@NotNull final String uri,
//                   Map<String, String> header,
//                   Object body,
//                   Class<T[]> clazz);
}
