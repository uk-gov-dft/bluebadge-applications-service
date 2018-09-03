package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class EligibilityValidatorTest extends ApplicationFixture {

  @Mock private WalkingValidator walkingValidator;
  @Mock private ArmsValidator armsValidator;
  @Mock private BenefitValidator benefitValidator;
  @Mock private BlindValidator blindValidator;

  private final EligibilityValidator eligibilityValidator;

  public EligibilityValidatorTest() {
    MockitoAnnotations.initMocks(this);
    eligibilityValidator =
        new EligibilityValidator(benefitValidator, armsValidator, walkingValidator, blindValidator);
  }

  @Test
  public void validateEligibilityByType_benefit() {
    // Given valid app
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build());

    validateEligibilityType(0, FieldKeys.KEY_ELI_BENEFIT);

    // specific eligibility validated
    verify(benefitValidator, times(1)).validate(app, errors);

    // Given required object was missing
    app.getEligibility().setBenefit(null);
    reset();
    // Get error
    validateEligibilityType(1, FieldKeys.KEY_ELI_BENEFIT);
    // specific eligibility validated
    verify(benefitValidator, times(1)).validate(app, errors);
  }

  @Test
  public void validateEligibilityType_Arms() {
    // Given valid app
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build());

    validateEligibilityType(0, FieldKeys.KEY_ELI_ARMS);

    // specific eligibility validated
    verify(armsValidator, times(1)).validate(app, errors);

    // Given required object was missing
    app.getEligibility().setDisabilityArms(null);
    reset();
    // Get error
    validateEligibilityType(1, FieldKeys.KEY_ELI_ARMS);
    // specific eligibility validated
    verify(armsValidator, times(1)).validate(app, errors);
  }

  @Test
  public void validateEligibilityType_Walk() {
    // Given valid app
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());

    validateEligibilityType(0, FieldKeys.KEY_ELI_WALKING);

    // specific eligibility validated
    verify(walkingValidator, times(1)).validate(app, errors);

    // Given required object was missing
    app.getEligibility().setWalkingDifficulty(null);
    reset();
    // Get error
    validateEligibilityType(1, FieldKeys.KEY_ELI_WALKING);
    // specific eligibility validated
    verify(walkingValidator, times(1)).validate(app, errors);
  }

  @Test
  public void validateEligibilityType_ChildBulk() {
    // Given valid app
    reset(
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityChildBulk().build());

    validateEligibilityType(0, FieldKeys.KEY_ELI_CHILD3);

    // Given required object was missing
    app.getEligibility().setChildUnder3(null);
    reset();
    // Get error
    validateEligibilityType(1, FieldKeys.KEY_ELI_CHILD3);
  }

  @Test
  public void validateEligibilityType_Blind() {
    // Given valid app
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build());

    validateEligibilityType(0, FieldKeys.KEY_ELI_BLIND);
    verify(blindValidator, times(1)).validate(any(), any());
  }

  private void validateEligibilityType(int expectedErrors, String field) {
    // When validated
    eligibilityValidator.validateEligibilityByType(app, errors);

    // Error counts match
    assertEquals(expectedErrors, errors.getErrorCount());
    assertEquals(expectedErrors, errors.getFieldErrorCount(field));
  }

  @Test
  public void failUnneededObjects() {
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityChildVehicle()
            .build());

    eligibilityValidator.failUnneededObjects(app, errors);
    assertEquals(0, errors.getErrorCount());
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
    assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BLIND));

    // Add objects.
    addArms(app);
    addBlind(app);
    addBenefit(app);
    addChild(app);
    addWalking(app);

    eligibilityValidator.failUnneededObjects(app, errors);
    assertEquals(5, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BLIND));
  }

  @Test
  public void validateConditionsDescription() {
    // Can only have discretionary conditions if discretionary eligibility
    // Valid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().setDescriptionOfConditions("www");
    reset();

    eligibilityValidator.validateConditionsDescription(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityAfrfcs().build();
    app.getEligibility().setDescriptionOfConditions("www");
    reset();

    eligibilityValidator.validateConditionsDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CONDITIONS_DESC));
  }

  @Test
  public void validateHealthcareProfessionals() {

    // Can only have healthcare pros if WALKD, CHILDBULK or CHILDVEHIC
    // Valid
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addHealthcarePro()
            .build());
    eligibilityValidator.validateHealthcareProfessionals(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    reset(
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityArms()
            .addHealthcarePro()
            .build());
    eligibilityValidator.validateHealthcareProfessionals(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_HEALTH_PROS));
  }

  @Test
  public void validateEligibility() {
    // Mostly check no null pointers.
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());
    eligibilityValidator.validate(app, errors);
  }

  @Test
  public void hasEligibility() {
    reset(getApplicationBuilder().addBaseApplication().build());
    errors.rejectValue(FieldKeys.KEY_ELIGIBILITY, "FFF");
    assertFalse(eligibilityValidator.hasEligibility(errors));

    reset(
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityChildBulk().build());
    assertTrue(eligibilityValidator.hasEligibility(errors));
  }
}
