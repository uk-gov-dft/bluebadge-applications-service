package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class ReferenceDataServiceConfiguration {

  @NotNull
  @Value("${referencedataservice.servicehost.scheme}")
  private String scheme;

  @NotNull
  @Value("${referencedataservice.servicehost.host}")
  private String host;

  @NotNull
  @Value("${referencedataservice.servicehost.port}")
  private Integer port;

  @Value("${referencedataservice.servicehost.connectiontimeout}")
  private Integer connectionTimeout;

  @Value("${referencedataservice.servicehost.requesttimeout}")
  private Integer requestTimeout;

  @NotNull
  @Value("${referencedataservice.servicehost.contextpath}")
  private String contextPath;

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setRequestTimeout(Integer requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public UriComponentsBuilder getUriComponentsBuilder(String... pathSegments) {
    return UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(host)
        .port(port)
        .path(contextPath)
        .pathSegment(pathSegments);
  }
}
