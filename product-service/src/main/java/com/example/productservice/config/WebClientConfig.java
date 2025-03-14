package com.example.productservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    public static final String DEFAULT_CLIENT_BEAN = "DEFAULT";

    @Value("${webClient.connection.timeoutInSec:30}")
    private int connectionTimeout;

    @Value("${webClient.request.timeoutInSec:30}")
    private long timeout;

    /**
     * config and create WebClient instance
     * @return
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean(DEFAULT_CLIENT_BEAN)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebClient webClient() {
        return webClientBuilder()
                .baseUrl("")
                .clientConnector(new ReactorClientHttpConnector(httpClient())) //set up connector using HttpClient
                .build();
    }

    /**
     * Create HttpClient instance
     * @return
     */
    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout * 1000)
                .responseTimeout(Duration.ofSeconds(timeout)) //response waiting time
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.SECONDS)));
    }
}
