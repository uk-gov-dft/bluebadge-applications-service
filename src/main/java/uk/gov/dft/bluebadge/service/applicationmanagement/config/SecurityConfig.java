package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import uk.gov.dft.bluebadge.common.security.BBAccessTokenConverter;
import uk.gov.dft.bluebadge.common.security.Permissions;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;

@SuppressWarnings("unused")
@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

  private static final String OAUTH_CHECK_TOKEN_URI = "/oauth/check_token";
  public static final String OAUTH_2_HAS_SCOPE_CITIZEN = "#oauth2.hasScope('citizen-webapp')";

  @Value("${blue-badge.auth-server.url}")
  private String authServerUrl;

  @Value("${blue-badge.auth-server.client-id}")
  private String clientId;

  @Value("${blue-badge.auth-server.client-secret}")
  private String clientSecret;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/applications")
        .access(OAUTH_2_HAS_SCOPE_CITIZEN)
        .antMatchers("/applications", "/applications/**")
        .hasAuthority(Permissions.VIEW_APPLICATION_DETAILS.getPermissionName())
        .anyRequest()
        .denyAll();
  }

  @Bean
  public RemoteTokenServices tokenService() {
    RemoteTokenServices tokenService = new RemoteTokenServices();
    tokenService.setCheckTokenEndpointUrl(authServerUrl + OAUTH_CHECK_TOKEN_URI);
    tokenService.setClientId(clientId);
    tokenService.setClientSecret(clientSecret);
    tokenService.setAccessTokenConverter(jwtAccessTokenConverter());
    return tokenService;
  }

  @SuppressWarnings("WeakerAccess")
  @Bean
  JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(accessTokenConverter());
    return converter;
  }

  @SuppressWarnings("WeakerAccess")
  @Bean
  BBAccessTokenConverter accessTokenConverter() {
    return new BBAccessTokenConverter();
  }

  @Bean
  @ConfigurationProperties("blue-badge.auth-server")
  ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
    return new ClientCredentialsResourceDetails();
  }

  @Bean
  public SecurityUtils securityUtils() {
    return new SecurityUtils();
  }
}
