package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

import java.time.LocalDate;
import java.time.Period;

public class BenefitValidatorTest extends ApplicationFixture{

  private BenefitValidator benefitValidator = new BenefitValidator();

  @Test
  public void validateBenefit() {
    // Expirydate cannot be in past.
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(app);
    app.getEligibility().getBenefit().setIsIndefinite(false);
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));

    // Expiry date can be in future
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().plus(Period.ofDays(1)));
    resetErrors(app);
    benefitValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // No expiry date if is indefinite
    app.getEligibility().getBenefit().setIsIndefinite(true);
    resetErrors(app);
    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));
  }

}