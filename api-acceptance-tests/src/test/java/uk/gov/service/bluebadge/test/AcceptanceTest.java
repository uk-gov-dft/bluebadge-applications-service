package uk.gov.service.bluebadge.test;

import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@RunWith(Karate.class)
@CucumberOptions(features = "classpath:features")
public class AcceptanceTest {
    // no-op, config class only
}