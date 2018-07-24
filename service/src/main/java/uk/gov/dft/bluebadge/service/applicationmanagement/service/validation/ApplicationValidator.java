package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator.FieldKeys.*;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

@Component
@Slf4j
public class ApplicationValidator implements Validator {

  private final ReferenceDataService referenceDataService;
  private static final String INVALID = "Invalid";

  class FieldKeys {
    public static final String PARTY_TYPE = "party.typeCode";
    public static final String ELIGIBILITY = "eligibility";
    static final String ELIGIBILITY_TYPE = "eligibility.typeCode";
    public static final String BENEFIT_EXPIRY_DATE = "eligibility.benefit.expiryDate";
    public static final String LA = "localAuthorityCode";
    public static final String ORGANISATION = "party.organisation";
    public static final String CHARITY_NO = "party.organisation.charityNumber";
    public static final String PERSON = "party.person";
    public static final String DOB = "party.person.dob";
    public static final String BENEFIT = "eligibility.benefit";
    public static final String ARMS = "eligibility.disabilityArms";
    public static final String ARMS_VEHICLE_ADAPTION =
        "eligibility.disabilityArms.adaptedVehicleDescription";
    public static final String WALKING = "eligibility.walkingDifficulty";
    public static final String WALKING_TYPE_CODES = "eligibility.walkingDifficulty.typeCodes";
    public static final String WALKING_OTHER_DESCRIPTION =
        "eligibility.walkingDifficulty.otherDescription";
    public static final String WALKING_SPEED = "eligibility.walkingDifficulty.walkingSpeedCode";
    public static final String CHILD = "eligibility.childUnder3";
    public static final String HEALTHCARE_PROS = "eligibility.healthcareProfessionals";
    public static final String CONDITIONS_DESCRIPTION = "eligibility.descriptionOfConditions";
    public static final String ARTIFACTS = "artifacts";
    public static final String BLIND_REGISTERED_AT = "eligibility.blind.registeredAtLaId";
  }

  @Autowired
  ApplicationValidator(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Application.class.equals(clazz);
  }

  /**
   * Check we have the minimum set of valid data to make further validation worthwhile.
   *
   * @param errors Spring Errors.
   * @param app The Application to validate.
   * @return true if can continue validation.
   */
  boolean checkRequiredObjectsExistToContinueValidation(Errors errors, Application app) {
    // Don't bother if root objects missing. These are picked up by jcr303 validation.
    if (null == app.getParty()) {
      return false;
    }

    // If party validation failed then skip rest of validation - don't know which path to take.
    // This should only be possible if party was null, if invalid would have failed to deserialize
    return errors.getFieldErrorCount(PARTY_TYPE) == 0 && null != app.getParty().getTypeCode();
  }

  void validateLocalAuthority(Application app, Errors errors) {
    if (!referenceDataService.isAuthorityCodeValid(app.getLocalAuthorityCode())) {
      errors.rejectValue(LA, INVALID, "Invalid local authority code.");
    }
  }

  void validatePerson(Application app, Errors errors) {
    String messagePrefix = "When party is PERSON";
    // Require Person and eligibility objects
    rejectIfEmptyOrWhitespace(errors, PERSON, messagePrefix);
    rejectIfEmptyOrWhitespace(errors, ELIGIBILITY, messagePrefix);
    rejectIfExists(errors, ORGANISATION, app.getParty().getOrganisation(), messagePrefix);
    if (null != app.getParty().getPerson()
        && null != app.getParty().getPerson().getDob()
        && app.getParty().getPerson().getDob().isAfter(LocalDate.now())) {
      errors.rejectValue(DOB, INVALID, "Date of birth cannot be in future.");
    }
  }

  void validateOrganisation(Application app, Errors errors) {
    String messagePrefix = "When party is ORG";
    rejectIfEmptyOrWhitespace(errors, ORGANISATION, messagePrefix);
    rejectIfExists(errors, PERSON, app.getParty().getPerson(), messagePrefix);
    rejectIfExists(errors, ELIGIBILITY, app.getEligibility(), messagePrefix);
    rejectIfExists(errors, ARTIFACTS, app.getArtifacts(), messagePrefix);
    if (null != app.getParty().getOrganisation()
        && !Boolean.TRUE.equals(app.getParty().getOrganisation().isIsCharity())
        && null != app.getParty().getOrganisation().getCharityNumber()) {
      errors.rejectValue(
          CHARITY_NO, INVALID, "Charity number can only be present if organisation is a charity.");
    }
  }

  @Override
  public void validate(Object target, Errors errors) {
    Application app = (Application) target;

    validateLocalAuthority(app, errors);

    // Don't continue if basic objects previously invalid due to bean validation.
    if (!checkRequiredObjectsExistToContinueValidation(errors, app)) return;

    boolean isPerson = PartyTypeCodeField.PERSON.equals(app.getParty().getTypeCode());
    if (isPerson) {
      validatePerson(app, errors);
      validateEligibility(app, errors);
    } else {
      validateOrganisation(app, errors);
    }
  }

  void validateEligibility(Application app, Errors errors) {
    // Validate if eligibility present and type ok.
    if (errors.getFieldErrorCount(FieldKeys.ELIGIBILITY) == 0
        && errors.getFieldErrorCount(FieldKeys.ELIGIBILITY_TYPE) == 0) {
      validateEligibilityByType(app, errors);
      validateEligibilityCommon(app, errors);
    }
  }

  void validateEligibilityByType(Application app, Errors errors) {

    String messagePrefix;
    switch (app.getEligibility().getTypeCode()) {
      case PIP:
      case DLA:
      case WPMS:
        messagePrefix = "When eligibility PIP, DLA or WPMS";
        rejectIfEmptyOrWhitespace(errors, BENEFIT, messagePrefix);
        validateBenefit(app, errors);
        rejectIfExists(errors, ARMS, app.getEligibility().getDisabilityArms(), messagePrefix);
        rejectIfExists(errors, WALKING, app.getEligibility().getWalkingDifficulty(), messagePrefix);
        rejectIfExists(errors, CHILD, app.getEligibility().getChildUnder3(), messagePrefix);
        break;
      case ARMS:
        messagePrefix = "When eligibility is ARMS";
        rejectIfExists(errors, BENEFIT, app.getEligibility().getBenefit(), messagePrefix);
        rejectIfEmptyOrWhitespace(errors, ARMS, messagePrefix);
        rejectIfExists(errors, WALKING, app.getEligibility().getWalkingDifficulty(), messagePrefix);
        rejectIfExists(errors, CHILD, app.getEligibility().getChildUnder3(), messagePrefix);
        validateArms(app, errors);
        break;
      case WALKD:
        messagePrefix = "When eligibility is WALKD";
        rejectIfExists(errors, BENEFIT, app.getEligibility().getBenefit(), messagePrefix);
        rejectIfExists(errors, ARMS, app.getEligibility().getDisabilityArms(), messagePrefix);
        rejectIfEmptyOrWhitespace(errors, WALKING, messagePrefix);
        rejectIfExists(errors, CHILD, app.getEligibility().getChildUnder3(), messagePrefix);
        validateWalking(app, errors);
        break;
      case CHILDBULK:
        messagePrefix = "When eligibility is CHILDBULK";
        rejectIfExists(errors, BENEFIT, app.getEligibility().getBenefit(), messagePrefix);
        rejectIfExists(errors, ARMS, app.getEligibility().getDisabilityArms(), messagePrefix);
        rejectIfExists(errors, WALKING, app.getEligibility().getWalkingDifficulty(), messagePrefix);
        rejectIfEmptyOrWhitespace(errors, CHILD, messagePrefix);
        break;
      case BLIND:
      case AFRFCS:
      case TERMILL:
      case CHILDVEHIC:
        messagePrefix = "When eligibility is BLIND, AFRFCS, TERMILL or CHILDVEH";
        rejectIfExists(errors, BENEFIT, app.getEligibility().getBenefit(), messagePrefix);
        rejectIfExists(errors, ARMS, app.getEligibility().getDisabilityArms(), messagePrefix);
        rejectIfExists(errors, WALKING, app.getEligibility().getWalkingDifficulty(), messagePrefix);
        rejectIfExists(errors, CHILD, app.getEligibility().getChildUnder3(), messagePrefix);
        break;
    }
  }

  void validateArms(Application app, Errors errors) {
    if (errors.getFieldErrorCount(ARMS) == 0
        && Boolean.TRUE != app.getEligibility().getDisabilityArms().isIsAdaptedVehicle()
        && null != app.getEligibility().getDisabilityArms().getAdaptedVehicleDescription()) {

      errors.rejectValue(
          ARMS_VEHICLE_ADAPTION,
          INVALID,
          ARMS_VEHICLE_ADAPTION + " cannot be entered if is adapted vehicle not true.");
    }
  }

  void validateEligibilityCommon(Application app, Errors errors) {

    EligibilityCodeField eligibilityType = app.getEligibility().getTypeCode();

    if (null != app.getEligibility().getDescriptionOfConditions()
        && !isDiscretionaryEligibility(eligibilityType)) {
      errors.rejectValue(
          CONDITIONS_DESCRIPTION,
          INVALID,
          CONDITIONS_DESCRIPTION + " is only valid for discretionary eligibility types.");
    }

    if (EligibilityCodeField.WALKD != eligibilityType
        && EligibilityCodeField.CHILDBULK != eligibilityType
        && EligibilityCodeField.CHILDVEHIC != eligibilityType
        && null != app.getEligibility().getHealthcareProfessionals()) {
      errors.rejectValue(
          HEALTHCARE_PROS,
          INVALID,
          HEALTHCARE_PROS
              + " can only be entered if eligibility in WALKD, CHILDBULK or CHILDVEHIC.");
    }

    if (EligibilityCodeField.BLIND == eligibilityType) {
      validateBlind(app, errors);
    }
  }

  void validateBlind(Application app, Errors errors) {
    if (null != app.getEligibility().getBlind()
        && null != app.getEligibility().getBlind().getRegisteredAtLaId()
        && EligibilityCodeField.BLIND != app.getEligibility().getTypeCode()) {
      errors.rejectValue(
          FieldKeys.BLIND_REGISTERED_AT,
          INVALID,
          "Registered at LA only allowed if eligibility is BLIND.");
    }
  }

  void validateWalking(Application app, Errors errors) {
    if (errors.getFieldErrorCount(WALKING) == 0) {
      // If exists then validate values.
      if (null == app.getEligibility().getWalkingDifficulty().getTypeCodes()
          || app.getEligibility().getWalkingDifficulty().getTypeCodes().isEmpty()) {
        errors.rejectValue(
            WALKING_TYPE_CODES,
            INVALID,
            "Must have at least 1 walking type code if eligibility is WALKDIFF.");
      } else if (!app.getEligibility()
              .getWalkingDifficulty()
              .getTypeCodes()
              .contains(WalkingDifficultyTypeCodeField.SOMELSE)
          && null != app.getEligibility().getWalkingDifficulty().getOtherDescription()) {
        // If walking difficulty does not have something else selected cant have other
        // description.
        errors.rejectValue(
            WALKING_OTHER_DESCRIPTION,
            INVALID,
            WALKING_OTHER_DESCRIPTION + " can only be present if SOMELSE selected as a type.");
      }
      // Walking speed only present if can walk, from walking length of time.
      // Walking time code is mandatory for walking
      Assert.notNull(
          app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode(),
          "If WALKD then time code should be not null");
      if (WalkingLengthOfTimeCodeField.CANTWALK
              == app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode()
          && null != app.getEligibility().getWalkingDifficulty().getWalkingSpeedCode()) {
        errors.rejectValue(
            WALKING_SPEED,
            INVALID,
            WALKING_SPEED + " can only be present if walking length of time is not can't walk.");
      }
    }
  }

  void validateBenefit(Application app, Errors errors) {
    if (null != app.getEligibility().getBenefit()
        && null != app.getEligibility().getBenefit().getExpiryDate()) {
      if (LocalDate.now().isAfter(app.getEligibility().getBenefit().getExpiryDate())) {
        errors.rejectValue(
            BENEFIT_EXPIRY_DATE, INVALID, "Benefit expiry date cannot be in the past.");
      }

      if (Boolean.TRUE.equals(app.getEligibility().getBenefit().isIsIndefinite())) {
        errors.rejectValue(
            BENEFIT_EXPIRY_DATE,
            INVALID,
            "Benefit expiry date cannot be entered if benefit is indefinite.");
      }
    }
  }

  private boolean isDiscretionaryEligibility(EligibilityCodeField type) {
    switch (type) {
      case WALKD:
      case ARMS:
      case CHILDVEHIC:
      case CHILDBULK:
      case TERMILL:
        return true;
      default:
        return false;
    }
  }

  private static void rejectIfExists(
      Errors errors, String field, Object value, String messagePrefix) {

    if (null != value) {
      errors.rejectValue(field, "NotValid", messagePrefix + ": " + field + " should be null ");
    }
  }

  private static void rejectIfEmptyOrWhitespace(Errors errors, String field, String messagePrefix) {
    Assert.notNull(messagePrefix, "messagePrefis must be provided");
    Assert.notNull(field, "field must be provided");
    ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, field, "NotNull", messagePrefix + ":" + field + " cannot be null.");
  }
}
