package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(200)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Turn off security. Needed until resource server config is applied
    http.csrf().disable().antMatcher("/**").authorizeRequests().anyRequest().permitAll();
  }
}
