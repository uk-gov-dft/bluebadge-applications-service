package uk.gov.service.bluebadge.test;

import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

// Suppress Sonarqube warning that class needs tests.
@SuppressWarnings("squid:S2187")
@RunWith(Karate.class)
@CucumberOptions(features = "classpath:features")
public class AcceptanceTest {
  // no-op, config class only
}
