package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALKING_BREATHLESSNESS_TYPES;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class BreathlessnessValidatorTest extends ApplicationFixture {
  private BreathlessnessValidator breathlessnessValidator = new BreathlessnessValidator();

  @Test
  public void validate() {
    // Invalid first - Not BREATH Walking Difficulty but want to set BREATHLESSNESS
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessness()
            .build());
    breathlessnessValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(KEY_ELI_WALKING_BREATHLESSNESS_TYPES));

    // Valid second
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .setBreathOnWalkingEligibility()
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
