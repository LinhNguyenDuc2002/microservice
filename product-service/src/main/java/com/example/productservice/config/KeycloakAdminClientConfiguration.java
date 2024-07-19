//package com.example.productservice.config;
//
//import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
//import org.keycloak.OAuth2Constants;
//import org.keycloak.adapters.KeycloakConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KeycloakAdminClientConfiguration {
//    private static final String SECRET_KEY_MAPPING = "client-secret";
//
//    @Autowired
//    private KeycloakSpringBootProperties keycloakProperties;
//
//    /**
//     * Load Keycloak configuration from application.properties or application.yml
//     * @return
//     */
//    @Bean
//    public KeycloakConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//    @Bean
//    public Keycloak keycloak() {
//        KeycloakBuilder builder =
//                KeycloakBuilder.builder()
//                        .serverUrl(keycloakProperties.getAuthServerUrl())
//                        .realm(keycloakProperties.getRealm())
//                        .clientId(keycloakProperties.getResource())
//                        .clientSecret(String.valueOf(keycloakProperties.getCredentials().get(SECRET_KEY_MAPPING)))
//                        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build());
//        return builder.build();
//    }
//}
