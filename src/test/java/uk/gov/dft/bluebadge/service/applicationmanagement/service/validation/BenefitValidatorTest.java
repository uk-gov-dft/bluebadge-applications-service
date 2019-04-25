package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class BenefitValidatorTest extends ApplicationFixture {

  private BenefitValidator benefitValidator = new BenefitValidator();

  @Test
  public void validateBenefit() {
    // Expirydate cannot be in past.
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build());
    app.getEligibility().getBenefit().setIsIndefinite(false);
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));

    // Expiry date can be in future
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().plus(Period.ofDays(1)));
    reset();
    benefitValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // No expiry date if is indefinite
    app.getEligibility().getBenefit().setIsIndefinite(true);
    reset(app);
    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));
  }
}
