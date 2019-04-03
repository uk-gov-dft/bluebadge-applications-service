package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import uk.gov.dft.bluebadge.common.esapi.EsapiFilter;
import uk.gov.dft.bluebadge.common.logging.JwtMdcFilter;
import uk.gov.dft.bluebadge.common.logging.VersionFilter;

@Configuration
@PropertySource(value = "classpath:version.properties")
public class FilterConfig {

  @Bean
  public EsapiFilter getEsapiFilter() {
    return new EsapiFilter();
  }

  @Bean
  public JwtMdcFilter getJwtMdcFilter() {
    return new JwtMdcFilter();
  }

  @Bean
  public VersionFilter getVersionFilter(@Value("${api.version}") @NotNull String apiVersion) {
    return new VersionFilter(apiVersion);
  }
}
