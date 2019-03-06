package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class WalkingValidatorTest extends ApplicationFixture {
  @Mock private BreathlessnessValidator breathlessnessValidatorMock;


  private WalkingValidator walkingValidator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    walkingValidator = new WalkingValidator(breathlessnessValidatorMock);
  }


  @Test
  public void validateWalkingSpeed() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());

    // Walking speed only present if can walk, from walking length of time.
    // And invalid, but not null
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.CANTWALK);
    reset();
    walkingValidator.validateWalkingSpeed(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_SPEED));
    // And valid
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.FEWMIN);
    reset();
    walkingValidator.validateWalkingSpeed(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingOtherDescription() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());

    // Cant have other description if type codes does not contain SOMELSE
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().getWalkingDifficulty().setOtherDescription("ABC");
    reset();
    walkingValidator.validateWalkingOtherDescription(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));

    // And the valid version
    app.getEligibility()
        .getWalkingDifficulty()
        .getTypeCodes()
        .add(WalkingDifficultyTypeCodeField.SOMELSE);
    reset();
    walkingValidator.validateWalkingOtherDescription(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalking() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());
    // Valid first
    walkingValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have at least 1 type code, null
    app.getEligibility().getWalkingDifficulty().setTypeCodes(null);
    reset();
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));

    // Must have at least 1 type code, 0
    app.getEligibility().getWalkingDifficulty().setTypeCodes(new ArrayList<>());
    reset();
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));
  }
}
