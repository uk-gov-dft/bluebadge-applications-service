package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class EligibilityValidatorTest extends ApplicationFixture {

  @Mock WalkingValidator walkingValidator;
  @Mock ArmsValidator armsValidator;
  @Mock BenefitValidator benefitValidator;

  private final EligibilityValidator eligibilityValidator;
  private Application app;

  public EligibilityValidatorTest() {
    super();
    eligibilityValidator =
        new EligibilityValidator(benefitValidator, armsValidator, walkingValidator);
  }

  @Test
  public void validateEligibilityByType_PIP_DLA_WPMS() {
    // PIP
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(app);
    expectBenefitOnly(app);

    // DLA
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityDla().build();
    resetErrors(app);
    expectBenefitOnly(app);

    // Wpms
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWpms().build();
    resetErrors(app);
    expectBenefitOnly(app);
  }

  private void expectBenefitOnly(Application app) {
    // Should have benefit. and not walking, arms or child
    addBenefit(app);
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setDisabilityArms(null);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have benefit
    app.getEligibility().setBenefit(null);
    resetErrors(app);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));

    // Reset
    addBenefit(app);

    // Cannot have arms, walking, or child
    addArms(app);
    addWalking(app);
    addChild(app);
    resetErrors(app);
    eligibilityValidator.validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
  }

  @Test
  public void validateEligibilityByType_ARMS() {

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    resetErrors(app);

    // Should have arms. and not benefit, walking or child
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setBenefit(null);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have arms
    app.getEligibility().setDisabilityArms(null);
    resetErrors(app);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));

    // Reset
    addArms(app);

    // Cannot have arms, walking, or child
    addBenefit(app);
    addWalking(app);
    addChild(app);
    resetErrors(app);
    eligibilityValidator.validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
  }

  @Test
  public void validateEligibilityByType_WALKD() {

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);

    // Should have walking. and not benefit, arms or child
    app.getEligibility().setDisabilityArms(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setBenefit(null);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have walk
    app.getEligibility().setWalkingDifficulty(null);
    resetErrors(app);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));

    // Reset
    addWalking(app);

    // Cannot have arms, benefit, or child
    addBenefit(app);
    addArms(app);
    addChild(app);
    resetErrors(app);
    eligibilityValidator.validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
  }

  @Test
  public void validateEligibilityByType_CHILDBULK() {

    app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityChildBulk().build();
    resetErrors(app);

    // Should have child. and not benefit, arms or walking
    app.getEligibility().setDisabilityArms(null);
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setBenefit(null);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have child
    app.getEligibility().setChildUnder3(null);
    resetErrors(app);
    eligibilityValidator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));

    // Reset
    addChild(app);

    // Cannot have arms, walking, or benefit
    addBenefit(app);
    addArms(app);
    addWalking(app);
    resetErrors(app);
    eligibilityValidator.validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
  }

  @Test
  public void validateEligibilityByType_BLIND_AFRFCS_TERMILL_CHILDVEHIC() {
    // BLIND
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    resetErrors(app);
    expectNoEligibilityChildObjects(app);

    // AFRFCS
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityAfrfcs().build();
    resetErrors(app);
    expectNoEligibilityChildObjects(app);

    // TERMILL
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityTermIll().build();
    resetErrors(app);
    expectNoEligibilityChildObjects(app);

    // CHILDVEHIC
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityChildVehicle()
            .build();
    resetErrors(app);
    expectNoEligibilityChildObjects(app);
  }

  private void expectNoEligibilityChildObjects(Application app) {
    // Cannot have arms, walking, benefit, or child
    addArms(app);
    addWalking(app);
    addChild(app);
    addBenefit(app);
    resetErrors(app);
    eligibilityValidator.validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
    assertEquals(4, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3));
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BENEFIT));
  }

  @Test
  public void validateConditionsDescription() {
    // Can only have discretionary conditions if discretionary eligibility
    // Valid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().setDescriptionOfConditions("www");
    resetErrors(app);

    eligibilityValidator.validateConditionsDescription(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityAfrfcs().build();
    app.getEligibility().setDescriptionOfConditions("www");
    resetErrors(app);

    eligibilityValidator.validateConditionsDescription(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CONDITIONS_DESC));
  }

  @Test
  public void validateHealthcareProfessionals() {

    // Can only have healthcare pros if WALKD, CHILDBULK or CHILDVEHIC
    // Valid
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityWalking()
            .addHealthcarePro()
            .build();
    resetErrors(app);
    eligibilityValidator.validateHealthcareProfessionals(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityArms()
            .addHealthcarePro()
            .build();
    resetErrors(app);
    eligibilityValidator.validateHealthcareProfessionals(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_HEALTH_PROS));
  }

  @Test
  public void validateEligibility() {
    // Mostly check no null pointers.
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    eligibilityValidator.validate(app, errors);
  }
}
