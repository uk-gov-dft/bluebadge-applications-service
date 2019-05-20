package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class BenefitValidatorTest extends ApplicationFixture {

  private BenefitValidator benefitValidator = new BenefitValidator();

  @Test
  public void validateBenefit() {
    // Expiry date cannot be in past.
    Application app =  getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    app.getEligibility().getBenefit().setIsIndefinite(false);
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));

    // Expiry date can be in future
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().plus(Period.ofDays(1)));
    errors = getNewBindingResult(app);

    benefitValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // No expiry date if is indefinite
    app.getEligibility().getBenefit().setIsIndefinite(true);
    errors = getNewBindingResult(app);

    benefitValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENE_EXPIRY_DT));
  }

  @Test
  public void validateBenefit_renewal() {
    Application app =  getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    app.getEligibility().getBenefit().setIsIndefinite(null);
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    app.setApplicationTypeCode(ApplicationTypeCodeField.RENEW);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    benefitValidator.validate(app, errors);
    // Is indefinite is not mandatory
    assertEquals(0, errors.getErrorCount());
  }
}
