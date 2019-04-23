package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.common.api.common.RestTemplateFactory;
import uk.gov.dft.bluebadge.common.api.common.ServiceConfiguration;
import uk.gov.dft.bluebadge.common.logging.LoggingAspect;

@Configuration
public class ApiConfig {

  @Validated
  @ConfigurationProperties("blue-badge.reference-data-service.service-host")
  @Bean
  public ServiceConfiguration referenceDataServiceConfiguration() {
    return new ServiceConfiguration();
  }

  @Validated
  @ConfigurationProperties("blue-badge.message-service.service-host")
  @Bean
  public ServiceConfiguration messageServiceConfiguration() {
    return new ServiceConfiguration();
  }

  @Bean("referenceDataServiceRestTemplate")
  RestTemplate referenceDataServiceRestTemplate(
      ClientCredentialsResourceDetails clientCredentialsResourceDetails,
      ServiceConfiguration referenceDataServiceConfiguration) {
    return RestTemplateFactory.getClientRestTemplate(
        clientCredentialsResourceDetails, referenceDataServiceConfiguration);
  }

  @Bean("messageServiceRestTemplate")
  RestTemplate messageServiceRestTemplate(
      ClientCredentialsResourceDetails clientCredentialsResourceDetails,
      ServiceConfiguration messageServiceConfiguration) {
    return RestTemplateFactory.getClientRestTemplate(
        clientCredentialsResourceDetails, messageServiceConfiguration);
  }

  @Bean
  LoggingAspect getControllerLoggingAspect() {
    return new LoggingAspect();
  }
}
