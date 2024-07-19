package com.example.userservice.config;

import com.example.userservice.security.oauthserver.AuthorizationExceptionHandler;
import com.example.userservice.security.oauthserver.client.ClientAuthenticationConverter;
import com.example.userservice.security.oauthserver.client.ClientSecretAuthenticationProvider;
import com.example.userservice.security.oauthserver.client.PublicClientAuthenticationProvider;
import com.example.userservice.security.oauthserver.token.CustomAuthenticationSuccessHandler;
import com.example.userservice.security.oauthserver.token.RefreshTokenAuthenticationConverter;
import com.example.userservice.security.oauthserver.token.RefreshTokenAuthenticationProvider;
import com.example.userservice.security.oauthserver.token.TokenAuthenticationConverter;
import com.example.userservice.security.oauthserver.token.TokenAuthenticationProvider;
import com.example.userservice.security.oauthserver.token.TokenRevocationAuthenticationProvider;
import com.example.userservice.security.resourceserver.JwtResourceServerCustomize;
import com.example.userservice.security.resourceserver.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //allow use @PreAuthorize, @Secured
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtResourceServerCustomize jwtResourceServerCustomize;

    @Autowired
    private TokenResolver tokenResolver;

    @Autowired
    private ClientAuthenticationConverter clientAuthenticationConverter;

    @Autowired
    private PublicClientAuthenticationProvider publicClientAuthenticationProvider;

    @Autowired
    private ClientSecretAuthenticationProvider clientSecretAuthenticationProvider;

    @Autowired
    private AuthorizationExceptionHandler authorizationExceptionHandler;

    @Autowired
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    @Autowired
    private RefreshTokenAuthenticationConverter refreshTokenAuthenticationConverter;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;

    @Autowired
    private TokenRevocationAuthenticationProvider tokenRevocationAuthenticationProvider;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * Authenticate user when login
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Remove prefix "ROLE_"
     * Therefore, when use @Secured or @PreAuthorize, don't need to use prefix "ROLE_" in front of roles
     * @return
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    /**
     * OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
     * When call this method, it will apply default security for endpoints:
     * ----------------
     * Default Endpoints
     * ----------------
     *
     * Authorization Endpoint           /oauth2/authorize
     * Token Endpoint                   /oauth2/token
     * Token Revocation                 /oauth2/revoke
     * Token Introspection              /oauth2/introspect
     * JWK Set Endpoint                 /oauth2/jwks
     * Authorization Server Metadata    /.well-known/oauth-authorization-server
     * OIDC Provider Configuration      /.well-known/openid-configuration
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                        // Redirect to the login page when not authenticated from the authorization endpoint
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )

                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtResourceServerCustomize) //handle JWT token
                        .bearerTokenResolver(tokenResolver) //how to get token
                )

                // Returning OAuth2AuthorizationServerConfigurer allows to customize configs regarding Authorization Server
                // examples: client, token store, token endpoint, authorization endpoint, ...
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()) // Enable OpenID Connect (OIDC) 1.0

                // Customize the logic for client authentication requests
                .clientAuthentication(clientAuth -> clientAuth //config client authentication
                        // Pre-processor
                        .authenticationConverters(initConverter(clientAuthenticationConverter))
                        // Main-processor
                        .authenticationProviders(initProvider(publicClientAuthenticationProvider, clientSecretAuthenticationProvider))
                        .errorResponseHandler(authorizationExceptionHandler)
                )

                // Customize the logic for OAuth2 access token requests
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        // Pre-processor
                        .accessTokenRequestConverters(initConverter(tokenAuthenticationConverter, refreshTokenAuthenticationConverter))
                        // Main-processor: used for authenticating the OAuth2AuthorizationGrantAuthenticationToken
                        .authenticationProviders(initProvider(tokenAuthenticationProvider, refreshTokenAuthenticationProvider))
                        // Post-processor
                        .accessTokenResponseHandler(authenticationSuccessHandler)
                        .errorResponseHandler(authorizationExceptionHandler)
                )

                // Customize the logic for OAuth2 revocation requests
                .tokenRevocationEndpoint(tokenRevocation -> tokenRevocation
                        // Main-processor: used for authenticating the OAuth2TokenRevocationAuthenticationToken
                        .authenticationProviders(initProvider(tokenRevocationAuthenticationProvider))
                        .errorResponseHandler(authorizationExceptionHandler)
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain standardSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) //disable CORS
                .csrf(csrf -> csrf.disable()) //disable CSRF
                .httpBasic(httpBasic -> httpBasic.disable()) //disable HTTP Basic Authentication

                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(tokenResolver)
                        .jwt(jwtResourceServerCustomize)
                )

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new BasicAuthenticationEntryPoint()) //handle authentication exception
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()) //handle access denied exception
                )

                .authorizeHttpRequests(authorize -> authorize //config authentication rules for requests
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers( "/user/verify").permitAll()
                        .requestMatchers( "/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Form login handles the redirect to the login page from the authorization server filter chain
                .formLogin(Customizer.withDefaults());

//        filter through jwtAuthenticationFilter() before UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Transfer authentication request from client to Authentication
     * @param converters
     * @return
     */
    private Consumer<List<AuthenticationConverter>> initConverter(AuthenticationConverter... converters) {
        return authConverters -> {
            // remove all default converters
            authConverters.clear();
            Collections.addAll(authConverters, converters);
        };
    }

    /**
     * Authenticate requests from client
     * Note: AuthenticationProvider is used to authenticate Authentication
     * @param providers
     * @return
     */
    private Consumer<List<AuthenticationProvider>> initProvider(AuthenticationProvider... providers) {
        return authProviders -> {
            // remove all default converters
            authProviders.clear();
            Collections.addAll(authProviders, providers);
        };
    }
}
