package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField.DANGER;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField.SOMELSE;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
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

    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
  }

  @Test
  public void validateWalkingSpeed() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    // Walking speed only present if can walk, from walking length of time.
    // And invalid, but not null
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.CANTWALK);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingSpeed(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_SPEED));
    // And valid
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.ONEFIVE);
    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingSpeed(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingPainDescription_notNull() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("abc");
    walkingDifficulty.setHealthProfessionsForFalls(true);

    walkingDifficulty.setPainDescription("ABC");
    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_PAIN_DESC));
  }

  @Test
  public void validateWalkingPainDescription_null() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription(null);
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_PAIN_DESC));

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("abc");
    walkingDifficulty.setHealthProfessionsForFalls(true);

    walkingDifficulty.setPainDescription(null);
    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalking_balance_descriptionAndFalls_notNull() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setBalanceDescription("ABC");
    walkingDifficulty.setHealthProfessionsForFalls(true);
    walkingDifficulty.setPainDescription(null);
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");

    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_FALLS));
  }

  @Test
  public void validateWalking_balance_descriptionAndFalls_null() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE));
    walkingDifficulty.setPainDescription(null);
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_BALANCE_FALLS));

    walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("abc");

    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalking_danger_descriptionAndHeartLung_notNull() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(DANGER));
    walkingDifficulty.setDangerousDescription("ABC");
    walkingDifficulty.setChestLungHeartEpilepsy(true);
    walkingDifficulty.setPainDescription(null);
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("ABC");

    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_CONDITIONS));
  }

  @Test
  public void validateWalking_danger_descriptionAndHeartLung_null() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(Lists.newArrayList(DANGER));
    walkingDifficulty.setPainDescription(null);
    walkingDifficulty.setOtherDescription(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_DESC));
    assertEquals(
        "errors:" + errors, 1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_DANGER_CONDITIONS));

    walkingDifficulty.setTypeCodes(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    walkingDifficulty.setPainDescription("abc");

    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingOtherDescription_notNull() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(ImmutableList.of(WalkingDifficultyTypeCodeField.BALANCE));
    // Cant have other description if type codes does not contain SOMELSE
    walkingDifficulty.setOtherDescription("ABC");
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));

    // And the valid version
    walkingDifficulty.setTypeCodes(ImmutableList.of(SOMELSE));
    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateWalkingOtherDescription_null() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    walkingDifficulty.setTypeCodes(ImmutableList.of(DANGER));
    walkingDifficulty.setOtherDescription(null);
    walkingDifficulty.setDangerousDescription("W");
    walkingDifficulty.setChestLungHeartEpilepsy(Boolean.TRUE);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    walkingDifficulty.setTypeCodes(ImmutableList.of(SOMELSE));
    errors = getNewBindingResult(app);
    walkingValidator.validateWalkingDifficulties(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_OTHER_DESC));
  }

  @Test
  public void validateWalking() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();
    // Valid first
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    walkingValidator.validate(app, errors);
    assertEquals("errors:" + errors, 0, errors.getErrorCount());

    // Must have at least 1 type code, null
    walkingDifficulty.setTypeCodes(null);
    errors = getNewBindingResult(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));

    // Must have at least 1 type code, 0
    walkingDifficulty.setTypeCodes(new ArrayList<>());
    errors = getNewBindingResult(app);
    walkingValidator.validate(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALK_TYPES));
  }
}
