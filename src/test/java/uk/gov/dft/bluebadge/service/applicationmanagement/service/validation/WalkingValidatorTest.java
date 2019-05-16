package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class WalkingValidatorTest extends ApplicationFixture {
  @Mock private BreathlessnessValidator breathlessnessValidatorMock;

  private WalkingValidator walkingValidator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    walkingValidator = new WalkingValidator(breathlessnessValidatorMock);

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();

    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("test pain desc");
    // override the defaults from the fixture
    walkingDifficulty.setOtherDescription(null);

    reset(app);
  }

  @Test
  public void validateWalkingSpeed() {
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
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.ONEFIVE);
    reset();
    walkingValidator.validateWalkingSpeed(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingPainDescription_notNull() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("abc");
    walkingDifficulty.setHealthProfessionsForFalls(true);

    walkingDifficulty.setPainDescription("ABC");
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_PAIN_DESC));
  }

  @Test
  public void validateWalkingPainDescription_null() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_PAIN_DESC));

    walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("abc");
    walkingDifficulty.setHealthProfessionsForFalls(true);

    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalking_balance_descriptionAndFalls_notNull() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("ABC");
    walkingDifficulty.setHealthProfessionsForFalls(true);
    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");

    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_FALLS));
  }

  @Test
  public void validateWalking_balance_descriptionAndFalls_null() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_FALLS));

    walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("abc");

    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalking_danger_descriptionAndHeartLung_notNull() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.DANGER));
    walkingDifficulty.setDangerousDescription("ABC");
    walkingDifficulty.setChestLungHeartEpilepsy(true);
    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");

    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_CONDITIONS));
  }

  @Test
  public void validateWalking_danger_descriptionAndHeartLung_null() {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.DANGER));
    walkingDifficulty.setPainDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_CONDITIONS));

    walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("abc");

    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingOtherDescription_notNull() {
    // Cant have other description if type codes does not contain SOMELSE
    app.getEligibility().getWalkingDifficulty().setOtherDescription("ABC");
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));

    // And the valid version
    app.getEligibility()
        .getWalkingDifficulty()
        .getTypeCodes()
        .add(WalkingDifficultyTypeCodeField.SOMELSE);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingOtherDescription_null() {
    app.getEligibility().getWalkingDifficulty().setOtherDescription(null);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    app.getEligibility()
        .getWalkingDifficulty()
        .getTypeCodes()
        .add(WalkingDifficultyTypeCodeField.SOMELSE);
    reset();
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));
  }

  @Test
  public void validateWalking() {
    // Valid first
    walkingValidator.validate(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

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
