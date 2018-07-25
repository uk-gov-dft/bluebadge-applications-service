package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifacts;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ApplicationValidatorTest extends ApplicationFixture {

  ApplicationValidator validator;
  BeanPropertyBindingResult errors;

  public ApplicationValidatorTest() {
    super();
    validator = new ApplicationValidator(referenceDataService);
  }

  @Test
  public void checkRequiredObjectsToContinueValid() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given party type, party, and contact are ok, then ok to validate.
    assertTrue(validator.hasParty(errors, application));
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_missing_party() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    errors.rejectValue(ApplicationValidator.FieldKeys.PARTY,"");

    // Given the check failed ensure main validate method does nothing
    // And particularly has no null pointers.
    validator.validate(application, errors);
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_invalid_party_type() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given party type failed bean validation can't validate further
    errors.rejectValue(ApplicationValidator.FieldKeys.PARTY_TYPE, "Invalid");
    application.getParty().setTypeCode(PartyTypeCodeField.PERSON);
    assertFalse(validator.hasParty(errors, application));
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void validateLocalAuthority() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given LA valid, no errors.
    validator.validateLocalAuthority(application, errors);
    assertEquals(0, errors.getErrorCount());

    // Given LA ivalid, then error
    application.setLocalAuthorityCode("ABC");
    validator.validateLocalAuthority(application, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.LA));
  }

  @Test
  public void validatePerson() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(application);

    // When person valid, no errors added.
    validator.validatePerson(application, errors);
    assertEquals(0, errors.getErrorCount());

    // When DOB in future get error
    application.getParty().getPerson().setDob(LocalDate.now().plus(Period.ofYears(1)));
    validator.validatePerson(application, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.DOB));

    // Reset
    application.getParty().getPerson().setDob(ValidValues.DOB);

    // When Person, organisation must be null
    addOrganisation(application);
    resetErrors(application);
    validator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ORGANISATION));

    // Reset
    application.getParty().setOrganisation(null);

    // When Person, eligibility required
    application.setEligibility(null);
    resetErrors(application);
    validator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ELIGIBILITY));

    // Reset
    setEligibilityPip(application);

    // When Person, person required
    application.getParty().setPerson(null);
    resetErrors(application);
    validator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.PERSON));
  }

  @Test
  public void validateOrganisation() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Valid
    validator.validateOrganisation(application, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have org object
    application.getParty().setOrganisation(null);
    validator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ORGANISATION));

    // Reset
    addOrganisation(application);

    // Cannot have person object
    addPerson(application);
    resetErrors(application);
    validator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.PERSON));

    // Reset
    application.getParty().setPerson(null);

    // Cannot have eligibility object
    setEligibilityPip(application);
    resetErrors(application);
    validator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ELIGIBILITY));

    // Reset
    application.setEligibility(null);

    // Cannot have charity number if not charity
    application.getParty().getOrganisation().setIsCharity(false);
    application.getParty().getOrganisation().setCharityNumber(ValidValues.CHARITY_NO);
    resetErrors(application);
    validator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHARITY_NO));

    // Reset
    application.getParty().getOrganisation().setIsCharity(true);

    // Cannot have artifacts
    application.setArtifacts(new Artifacts());
    resetErrors(application);
    validator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARTIFACTS));
  }

  @Test
  public void validateEligibilityByType_PIP_DLA_WPMS() {
    // PIP
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(application);
    expectBenefitOnly(application);

    // DLA
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityDla().build();
    resetErrors(application);
    expectBenefitOnly(application);

    // Wpms
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWpms().build();
    resetErrors(application);
    expectBenefitOnly(application);
  }

  private void expectBenefitOnly(Application app) {
    // Should have benefit. and not walking, arms or child
    addBenefit(app);
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setDisabilityArms(null);
    validator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have benefit
    app.getEligibility().setBenefit(null);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT));

    // Reset
    addBenefit(app);

    // Cannot have arms, walking, or child
    addArms(app);
    addWalking(app);
    addChild(app);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHILD));
  }

  @Test
  public void validateEligibilityByType_ARMS() {

    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    resetErrors(app);

    // Should have arms. and not benefit, walking or child
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setBenefit(null);
    validator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have arms
    app.getEligibility().setDisabilityArms(null);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS));

    // Reset
    addArms(app);

    // Cannot have arms, walking, or child
    addBenefit(app);
    addWalking(app);
    addChild(app);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHILD));
  }

  @Test
  public void validateEligibilityByType_WALKD() {

    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);

    // Should have walking. and not benefit, arms or child
    app.getEligibility().setDisabilityArms(null);
    app.getEligibility().setChildUnder3(null);
    app.getEligibility().setBenefit(null);
    validator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have walk
    app.getEligibility().setWalkingDifficulty(null);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING));

    // Reset
    addWalking(app);

    // Cannot have arms, benefit, or child
    addBenefit(app);
    addArms(app);
    addChild(app);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHILD));
  }

  @Test
  public void validateEligibilityByType_CHILDBULK() {

    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityChildBulk().build();
    resetErrors(app);

    // Should have child. and not benefit, arms or walking
    app.getEligibility().setDisabilityArms(null);
    app.getEligibility().setWalkingDifficulty(null);
    app.getEligibility().setBenefit(null);
    validator.validateEligibilityByType(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have child
    app.getEligibility().setChildUnder3(null);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHILD));

    // Reset
    addChild(app);

    // Cannot have arms, walking, or benefit
    addBenefit(app);
    addArms(app);
    addWalking(app);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(3, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING));
  }

  @Test
  public void validateEligibilityByType_BLIND_AFRFCS_TERMILL_CHILDVEHIC() {
    // BLIND
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    resetErrors(application);
    expectNoEligibilityChildObjects(application);

    // AFRFCS
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityAfrfcs().build();
    resetErrors(application);
    expectNoEligibilityChildObjects(application);

    // TERMILL
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityTermIll().build();
    resetErrors(application);
    expectNoEligibilityChildObjects(application);

    // CHILDVEHIC
    application =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityChildVehicle()
            .build();
    resetErrors(application);
    expectNoEligibilityChildObjects(application);
  }

  private void expectNoEligibilityChildObjects(Application app) {
    // Cannot have arms, walking, benefit, or child
    addArms(app);
    addWalking(app);
    addChild(app);
    addBenefit(app);
    resetErrors(app);
    validator.validateEligibilityByType(app, errors);
    assertEquals(4, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CHILD));
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT));
  }

  @Test
  public void validateArms() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    resetErrors(app);

    // Valid first
    validator.validateArms(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Cant have adapted vehicle text if not adapted
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(false);
    app.getEligibility().getDisabilityArms().setAdaptedVehicleDescription("ddd");
    resetErrors(app);
    validator.validateArms(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(
        1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.ARMS_VEHICLE_ADAPTION));
  }

  @Test
  public void validateEligibilityCommon() {
    // Can only have discretionary conditions if discretionary eligibility
    // Valid
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().setDescriptionOfConditions("www");
    resetErrors(app);

    validator.validateEligibilityCommon(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityAfrfcs().build();
    app.getEligibility().setDescriptionOfConditions("www");
    resetErrors(app);

    validator.validateEligibilityCommon(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(
        1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.CONDITIONS_DESCRIPTION));

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
    validator.validateEligibilityCommon(app, errors);
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
    validator.validateEligibilityCommon(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.HEALTHCARE_PROS));
  }

  @Test
  public void validateWalking() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    // Valid first
    validator.validateWalking(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have at least 1 type code, null
    app.getEligibility().getWalkingDifficulty().setTypeCodes(null);
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING_TYPE_CODES));

    // Must have at least 1 type code, 0
    app.getEligibility().getWalkingDifficulty().setTypeCodes(new ArrayList<>());
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING_TYPE_CODES));

    // Cant have other description if type codes does not contain SOMELSE
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    app.getEligibility().getWalkingDifficulty().setOtherDescription("ABC");
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(
        1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING_OTHER_DESCRIPTION));

    // And the valid version
    app.getEligibility()
        .getWalkingDifficulty()
        .getTypeCodes()
        .add(WalkingDifficultyTypeCodeField.SOMELSE);
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Walking speed only present if can walk, from walking length of time.
    // And invalid, but not null
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.CANTWALK);
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.WALKING_SPEED));
    // And valid
    app.getEligibility()
        .getWalkingDifficulty()
        .setWalkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.FEWMIN);
    resetErrors(app);
    validator.validateWalking(app, errors);
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateBenefit() {
    // Expirydate cannot be in past.
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(app);
    app.getEligibility().getBenefit().setIsIndefinite(false);
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    validator.validateBenefit(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT_EXPIRY_DATE));

    // Expiry date can be in future
    app.getEligibility().getBenefit().setExpiryDate(LocalDate.now().plus(Period.ofDays(1)));
    resetErrors(app);
    validator.validateBenefit(app, errors);
    assertEquals(0, errors.getErrorCount());

    // No expiry date if is indefinite
    app.getEligibility().getBenefit().setIsIndefinite(true);
    resetErrors(app);
    validator.validateBenefit(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BENEFIT_EXPIRY_DATE));
  }

  @Test
  public void validateBlind() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    resetErrors(app);

    // Valid
    validator.validateBlind(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Invalid
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    addBlind(app);
    resetErrors(app);
    validator.validateBlind(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(ApplicationValidator.FieldKeys.BLIND_REGISTERED_AT));
  }

  @Test
  public void validateEligibility() {
    // Mostly check no null pointers.
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    validator.validateEligibility(app, errors);
  }

  @Test
  public void validate() {
    // Couple of runs through the whole validate to check safe for null pointers.
    Application app = getApplicationBuilder().addBaseApplication().build();
    resetErrors(app);
    validator.validate(app, errors);

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    validator.validate(app, errors);

    app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(app);
    validator.validate(app, errors);
  }

  void resetErrors(Application application) {
    errors = new BeanPropertyBindingResult(application, "application");
  }
}
