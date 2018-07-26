package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

import java.util.ArrayList;

public class WalkingValidatorTest extends ApplicationFixture {

  private WalkingValidator walkingValidator = new WalkingValidator();

  @Test
  public void validateWalking() {
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    // Valid first
    walkingValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have at least 1 type code, null
    app.getEligibility().getWalkingDifficulty().setTypeCodes(null);
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));

    // Must have at least 1 type code, 0
    app.getEligibility().getWalkingDifficulty().setTypeCodes(new ArrayList<>());
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));

    // Cant have other description if type codes does not contain SOMELSE
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().getWalkingDifficulty().setOtherDescription("ABC");
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));

    // And the valid version
    app.getEligibility()
        .getWalkingDifficulty()
        .getTypeCodes()
        .add(WalkingDifficultyTypeCodeField.SOMELSE);
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Walking speed only present if can walk, from walking length of time.
    // And invalid, but not null
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.CANTWALK);
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_SPEED));
    // And valid
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.FEWMIN);
    resetErrors(app);
    walkingValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

}