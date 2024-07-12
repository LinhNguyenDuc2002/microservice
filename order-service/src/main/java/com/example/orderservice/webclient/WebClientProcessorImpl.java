package com.example.orderservice.webclient;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class WebClientProcessorImpl implements WebClientProcessor {
    @Autowired
    private WebClient webClient;

    @Override
    public <T> T get(String uri, Map<String, String> header, MultiValueMap<String, String> queryParam, Class<T> clazz) throws Exception {
        final Map<String, String> requestHeader = header == null ? Collections.emptyMap() : header;
        final MultiValueMap<String, String> requestParam = queryParam == null ? new LinkedMultiValueMap<>() : queryParam;

        try {
            return webClient.get()
                    .uri(uri, uriBuilder -> uriBuilder
                            .queryParams(requestParam)
                            .build())
                    .headers(h -> h.setAll(requestHeader))
                    .retrieve() //receive response from server
                    .bodyToMono(clazz)
                    .doOnError(e -> log.error("Failed to get the data: " + e.getMessage()))
                    .block();
        } catch (WebClientResponseException e) {
            handleWebClientException(e);
        } catch (Exception e) {
            log.error("Exception occurred: " + e.getMessage());
            throw new Exception("Could not retrieve the data");
        }

        return null;
    }

    @Override
    public <T> T patch(String uri, Map<String, String> header, Object body, Class<T> clazz) throws Exception {
        final Map<String, String> requestHeader = header == null ? Collections.emptyMap() : header;

        try {
            return webClient.patch()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(h -> h.setAll(requestHeader))
                    .body(Mono.just(body), Object.class)
                    .retrieve()
                    .bodyToMono(clazz)
                    .doOnError(e -> log.error("Failed to initialize the data: " + e.getMessage()))
                    .block();
        } catch (WebClientResponseException e) {
            handleWebClientException(e);
        } catch (Exception e) {
            log.error("Exception occurred: " + e.getMessage());
            throw new Exception("Could not initialize the data");
        }

        return null;
    }

    private void handleWebClientException(WebClientResponseException e) throws Exception {
        int status = e.getStatusCode().value();
        if (status == HttpStatus.UNAUTHORIZED.value()) {
            throw new AccessDeniedException("");
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            throw new AccessDeniedException("");
        } else {
            JSONObject errorMsg = e.getResponseBodyAs(JSONObject.class);
            throw new Exception(getErrorMsg(errorMsg));
        }
    }

    private String getErrorMsg(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "Unknown error";
        }

        if (jsonObject.containsKey("error")) {
            return jsonObject.getAsString("error");
        }

        if (jsonObject.containsKey("error_message")) {
            return jsonObject.getAsString("error_message");
        }

        return jsonObject.toJSONString();
    }
}
