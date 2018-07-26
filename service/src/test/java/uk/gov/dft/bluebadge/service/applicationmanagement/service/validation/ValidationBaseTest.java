package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ValidationBaseTest extends ApplicationFixture{

  Application application;

  @Test
  public void rejectIfExists() {}

  @Test
  public void rejectIfEmptyOrWhitespace() {}

  @Test
  public void hasNoFieldErrors() {}

  @Test
  public void hasFieldErrors() {}

  @Test
  public void exists() {}

  @Test
  public void notExists() {}

  @Test
  public void hasText() {}
}