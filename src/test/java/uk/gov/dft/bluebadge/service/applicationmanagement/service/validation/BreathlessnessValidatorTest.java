package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALKING_BREATHLESSNESS_TYPES;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class BreathlessnessValidatorTest extends ApplicationFixture {
  private BreathlessnessValidator breathlessnessValidator = new BreathlessnessValidator();

  @Test
  public void validate() {
    // Invalid first - Not BREATH Walking Difficulty but want to set BREATHLESSNESS
    Application app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessness()
            .build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    breathlessnessValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(KEY_ELI_WALKING_BREATHLESSNESS_TYPES));

    // Valid second
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .setBreathOnWalkingEligibility()
            .addBreathlessness()
            .build();
    errors = getNewBindingResult(app);
    breathlessnessValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateBreathlessnessOtherDescription() {
    // Valid first
    Application app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOther()
            .build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(0, errors.getErrorCount());
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));

    // Invalid second
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOtherDescriptionOnly()
            .build();
    errors = getNewBindingResult(app);
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));

    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addBreathlessnessOther()
            .build();
    errors = getNewBindingResult(app);
    app.getEligibility().getWalkingDifficulty().getBreathlessness().setOtherDescription("");
    breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));
  }
}
