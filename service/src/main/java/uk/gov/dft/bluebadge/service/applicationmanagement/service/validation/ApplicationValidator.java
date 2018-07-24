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

  interface FieldKeys {
    String PARTY_TYPE = "party.typeCode";
    String ELIGIBILITY = "eligibility";
    String ELIGIBILITY_TYPE = "eligibility.typeCode";
    String BENEFIT_EXPIRY_DATE = "eligibility.benefit.expiryDate";
    String LA = "localAuthorityCode";
    String ORGANISATION = "party.organisation";
    String CHARITY_NO = "party.organisation.charityNumber";
    String PERSON = "party.person";
    String DOB = "party.person.dob";
    String BENEFIT = "eligibility.benefit";
    String ARMS = "eligibility.disabilityArms";
    String ARMS_VEHICLE_ADAPTION = "eligibility.disabilityArms.adaptedVehicleDescription";
    String WALKING = "eligibility.walkingDifficulty";
    String WALKING_TYPE_CODES = "eligibility.walkingDifficulty.typeCodes";
    String WALKING_OTHER_DESCRIPTION = "eligibility.walkingDifficulty.otherDescription";
    String WALKING_SPEED = "eligibility.walkingDifficulty.walkingSpeedCode";
    String CHILD = "eligibility.childUnder3";
    String HEALTHCARE_PROS = "eligibility.healthcareProfessionals";
    String CONDITIONS_DESCRIPTION = "eligibility.descriptionOfConditions";
    String ARTIFACTS = "artifacts";
    String BLIND_REGISTERED_AT = "eligibility.blind.registeredAtLaId";
  }

  @Autowired
  public ApplicationValidator(ReferenceDataService referenceDataService) {
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
      rejectValue(errors, LA, "Invalid", "Invalid local authority code.");
    }
  }

  void validatePerson(Application app, Errors errors) {
    String messagePrefix = "When party is PERSON";
    // Require Person and eligibility objects
    rejectIfEmptyOrWhitespace(errors, PERSON, messagePrefix);
    rejectIfEmptyOrWhitespace(errors, ELIGIBILITY, messagePrefix);
    rejectIfExists(errors, ORGANISATION, messagePrefix);
    if (null != app.getParty().getPerson()
        && null != app.getParty().getPerson().getDob()
        && app.getParty().getPerson().getDob().isAfter(LocalDate.now())) {
      rejectValue(errors, DOB, "Invalid", "Date of birth cannot be in future.");
    }
  }

  void validateOrganisation(Application app, Errors errors) {
    String messagePrefix = "When party is ORG";
    rejectIfEmptyOrWhitespace(errors, ORGANISATION, messagePrefix);
    rejectIfExists(errors, PERSON, messagePrefix);
    rejectIfExists(errors, ELIGIBILITY, messagePrefix);
    rejectIfExists(errors, ARTIFACTS, messagePrefix);
    if (null != app.getParty().getOrganisation()
        && !Boolean.TRUE.equals(app.getParty().getOrganisation().isIsCharity())
        && null != app.getParty().getOrganisation().getCharityNumber()) {
      rejectValue(
          errors,
          CHARITY_NO,
          "Invalid",
          "Charity number can only be present if organisation is a charity.");
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
        rejectIfExists(errors, ARMS, messagePrefix);
        rejectIfExists(errors, WALKING, messagePrefix);
        rejectIfExists(errors, CHILD, messagePrefix);
        break;
      case ARMS:
        messagePrefix = "When eligibility is ARMS";
        rejectIfExists(errors, BENEFIT, messagePrefix);
        rejectIfEmptyOrWhitespace(errors, ARMS, messagePrefix);
        rejectIfExists(errors, WALKING, messagePrefix);
        rejectIfExists(errors, CHILD, messagePrefix);
        validateArms(app, errors);
        break;
      case WALKD:
        messagePrefix = "When eligibility is WALKD";
        rejectIfExists(errors, BENEFIT, messagePrefix);
        rejectIfExists(errors, ARMS, messagePrefix);
        rejectIfEmptyOrWhitespace(errors, WALKING, messagePrefix);
        rejectIfExists(errors, CHILD, messagePrefix);
        validateWalking(app, errors);
        break;
      case CHILDBULK:
        messagePrefix = "When eligibility is CHILDBULK";
        rejectIfExists(errors, BENEFIT, messagePrefix);
        rejectIfExists(errors, ARMS, messagePrefix);
        rejectIfExists(errors, WALKING, messagePrefix);
        rejectIfEmptyOrWhitespace(errors, CHILD, messagePrefix);
        break;
      case BLIND:
        validateBlind(app, errors);
      case AFRFCS:
      case TERMILL:
      case CHILDVEHIC:
        messagePrefix = "When eligibility is BLIND, AFRFCS, TERMILL or CHILDVEH";
        rejectIfExists(errors, BENEFIT, messagePrefix);
        rejectIfExists(errors, ARMS, messagePrefix);
        rejectIfExists(errors, WALKING, messagePrefix);
        rejectIfExists(errors, CHILD, messagePrefix);
        break;
    }
  }

  void validateArms(Application app, Errors errors) {
    if (errors.getFieldErrorCount(ARMS) == 0
        && Boolean.TRUE != app.getEligibility().getDisabilityArms().isIsAdaptedVehicle()
        && null != app.getEligibility().getDisabilityArms().getAdaptedVehicleDescription()) {
      rejectValue(
          errors,
          ARMS_VEHICLE_ADAPTION,
          "Invalid",
          ARMS_VEHICLE_ADAPTION + " cannot be entered if is adapted vehicle not true.");
    }
  }

  void validateEligibilityCommon(Application app, Errors errors) {

    EligibilityCodeField eligibilityType = app.getEligibility().getTypeCode();

    if (null != app.getEligibility().getDescriptionOfConditions()
        && !isDiscretionaryEligibility(eligibilityType)) {
      rejectValue(
          errors,
          CONDITIONS_DESCRIPTION,
          "Invalid",
          CONDITIONS_DESCRIPTION + " is only valid for discretionary eligibility types.");
    }

    if (EligibilityCodeField.WALKD != eligibilityType
        && EligibilityCodeField.CHILDBULK != eligibilityType
        && EligibilityCodeField.CHILDVEHIC != eligibilityType
        && null != app.getEligibility().getHealthcareProfessionals()) {
      rejectValue(
          errors,
          HEALTHCARE_PROS,
          "Invalid",
          HEALTHCARE_PROS
              + " can only be entered if eligibility in WALKD, CHILDBULK or CHILDVEHIC.");
    }
  }

  void validateBlind(Application app, Errors errors) {
    if (null != app.getEligibility().getBlind()
        && null != app.getEligibility().getBlind().getRegisteredAtLaId()
        && EligibilityCodeField.BLIND != app.getEligibility().getTypeCode()) {
      rejectValue(
          errors,
          FieldKeys.BLIND_REGISTERED_AT,
          "Invalid",
          "Registered at LA only allowed if eligibility is BLIND.");
    }
  }

  void validateWalking(Application app, Errors errors) {
    if (errors.getFieldErrorCount(WALKING) == 0) {
      // If exists then validate values.
      if (null == app.getEligibility().getWalkingDifficulty().getTypeCodes()
          || app.getEligibility().getWalkingDifficulty().getTypeCodes().isEmpty()) {
        rejectValue(
            errors,
            WALKING_TYPE_CODES,
            "Invalid",
            "Must have at least 1 walking type code if eligibility is WALKDIFF.");
      } else if (!app.getEligibility()
              .getWalkingDifficulty()
              .getTypeCodes()
              .contains(WalkingDifficultyTypeCodeField.SOMELSE)
          && null != app.getEligibility().getWalkingDifficulty().getOtherDescription()) {
        // If walking difficulty does not have something else selected cant have other
        // description.
        rejectValue(
            errors,
            WALKING_OTHER_DESCRIPTION,
            "Invalid",
            WALKING_OTHER_DESCRIPTION + " can only be present if SOMELSE selected as a type.");
      }
      // Walking speed only present if can walk, from walking length of time.
      if ((null == app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode()
              || WalkingLengthOfTimeCodeField.CANTWALK
                  == app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode())
          && null != app.getEligibility().getWalkingDifficulty().getWalkingSpeedCode()) {
        rejectValue(
            errors,
            WALKING_SPEED,
            "Invalid",
            WALKING_SPEED + " can only be present if walking length of time is not can't walk.");
      }
    }
  }

  void validateBenefit(Application app, Errors errors) {
    if (null != app.getEligibility().getBenefit()
        && null != app.getEligibility().getBenefit().getExpiryDate()) {
      if (LocalDate.now().isAfter(app.getEligibility().getBenefit().getExpiryDate())) {
        rejectValue(
            errors, BENEFIT_EXPIRY_DATE, "Invalid", "Benefit expiry date cannot be in the past.");
      }
      if (null != app.getEligibility().getBenefit().isIsIndefinite()
          && app.getEligibility().getBenefit().isIsIndefinite() == Boolean.TRUE) {
        rejectValue(
            errors,
            BENEFIT_EXPIRY_DATE,
            "Invalid",
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
    }
    return false;
  }

  private static void rejectValue(Errors errors, String field, String reason, String message) {
    errors.rejectValue(field, reason, message);
  }

  private static void rejectIfExists(Errors errors, String field, String messagePrefix) {
    Object object = errors.getFieldValue(field);

    if (null != object) {
      rejectValue(errors, field, "NotValid", messagePrefix + ": " + field + " should be null ");
    }
  }

  private static void rejectIfEmptyOrWhitespace(Errors errors, String field, String messagePrefix) {
    Assert.notNull(messagePrefix, "messagePrefis must be provided");
    Assert.notNull(field, "field must be provided");
    ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, field, "NotNull", messagePrefix + ":" + field + " cannot be null.");
  }
}
