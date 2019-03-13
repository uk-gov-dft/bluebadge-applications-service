package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class BreathlessnessValidatorTest extends ApplicationFixture {
  private BreathlessnessValidator breathlessnessValidator = new BreathlessnessValidator();

  @Test
  public void validate() {
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessness()
            .build());
    breathlessnessValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateBreathlessnessOtherDescription() {
    // Valid first
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOther()
            .build());
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(0, errors.getErrorCount());
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));

    // Invalid second
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOtherDescriptionOnly()
            .build());
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));

    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOther()
            .build());
    app.getEligibility().getWalkingDifficulty().getBreathlessness().setOtherDescription("");
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));
  }
}
