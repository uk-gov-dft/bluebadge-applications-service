package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static java.lang.Boolean.TRUE;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.TERMILL;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WPMS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELIGIBILITY;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_ARMS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENEFIT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENE_EXPIRY_DT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BLIND;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CONDITIONS_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_HEALTH_PROS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALKING;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_OTHER_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_SPEED;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_TYPES;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.Matchers.collection;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.Matchers.enumValues;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ValidationBase.ErrorTypes.NOT_VALID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;

import java.time.LocalDate;

@Component
public class EligibilityValidator extends ValidationBase {

  private BenefitValidator benefitValidator;
  private ArmsValidator armsValidator;
  private WalkingValidator walkingValidator;

  @Autowired
  public EligibilityValidator(BenefitValidator benefitValidator, ArmsValidator armsValidator, WalkingValidator walkingValidator) {
    this.benefitValidator = benefitValidator;
    this.armsValidator = armsValidator;
    this.walkingValidator = walkingValidator;
  }

  void validate(Application app, Errors errors) {
    // Validate if eligibility present and type ok, if not can't continue.
    if (hasEligibility(errors)) {

      // If ok then validate the eligibility
      // Most are type specific
      validateEligibilityByType(app, errors);
      // And can't have objects relating to other types
      validateNoUnexpectedEligibilityTypeObjects(app.getEligibility().getTypeCode(), errors);
      // A couple of rules are outside common type groupings
      validateHealthcareProfessionals(app, errors);
      validateConditionsDescription(app, errors);
    }
  }

  /**
   * Has sufficient eligibility fields to validate. Save checking nullability all over the place.
   *
   * @param errors Errors object holding previous validation.
   * @return true if no eligibility errors.
   */
  boolean hasEligibility(Errors errors) {
    return hasNoFieldErrors(errors, KEY_ELIGIBILITY)
        && hasNoFieldErrors(errors, FieldKeys.KEY_ELI_TYPE);
  }

  void validateEligibilityByType(Application app, Errors errors) {

    String messagePrefix = "For eligibility type " + app.getEligibility().getTypeCode();
    switch (app.getEligibility().getTypeCode()) {
      case PIP:
      case DLA:
      case WPMS:
        rejectIfEmptyOrWhitespace(errors, KEY_ELI_BENEFIT, messagePrefix);
        benefitValidator.validate(app, errors);
        break;
      case ARMS:
        rejectIfEmptyOrWhitespace(errors, KEY_ELI_ARMS, messagePrefix);
        armsValidator.validate(app, errors);
        break;
      case WALKD:
        rejectIfEmptyOrWhitespace(errors, KEY_ELI_WALKING, messagePrefix);
        walkingValidator.validate(app, errors);
        break;
      case CHILDBULK:
        rejectIfEmptyOrWhitespace(errors, KEY_ELI_CHILD3, messagePrefix);
        break;
      case BLIND:
        // Don't necessarily require blind object, no mandatory fields or special validation
      case AFRFCS:
      case TERMILL:
      case CHILDVEHIC:
        break;
    }
  }

  void validateNoUnexpectedEligibilityTypeObjects(EligibilityCodeField type, Errors errors) {
    String messagePrefix = "For eligibility type " + type;
    // Blind
    if (!EligibilityCodeField.BLIND.equals(type)) {
      rejectIfExists(errors, KEY_ELI_BLIND, messagePrefix);
    }

    // Benefit
    if (enumValues(PIP, DLA, WPMS).doesNotContain(type)) {
      rejectIfExists(errors, KEY_ELI_BENEFIT, messagePrefix);
    }

    // Arms
    if (!EligibilityCodeField.ARMS.equals(type)) {
      rejectIfExists(errors, KEY_ELI_ARMS, messagePrefix);
    }

    // Walking
    if (!EligibilityCodeField.WALKD.equals(type)) {
      rejectIfExists(errors, KEY_ELI_WALKING, messagePrefix);
    }

    // Child
    if (!EligibilityCodeField.CHILDBULK.equals(type)) {
      rejectIfExists(errors, KEY_ELI_CHILD3, messagePrefix);
    }
  }

  /**
   * Conditions only entered for discretionary eligibility.
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateConditionsDescription(Application app, Errors errors) {

    EligibilityCodeField eligibilityType = app.getEligibility().getTypeCode();

    if (exists(errors, FieldKeys.KEY_ELI_CONDITIONS_DESC)
        && !isDiscretionaryEligibility(eligibilityType)) {
      errors.rejectValue(
          KEY_ELI_CONDITIONS_DESC,
          NOT_VALID,
          KEY_ELI_CONDITIONS_DESC + " is only valid for discretionary eligibility types.");
    }
  }

  void validateHealthcareProfessionals(Application app, Errors errors) {
    if (!enumValues(CHILDBULK, CHILDVEHIC, WALKD).contains(app.getEligibility().getTypeCode())
        && collection(app.getEligibility().getHealthcareProfessionals()).isNotEmpty()) {
      errors.rejectValue(
          KEY_ELI_HEALTH_PROS,
          NOT_VALID,
          KEY_ELI_HEALTH_PROS
              + " can only be entered if eligibility in WALKD, CHILDBULK or CHILDVEHIC.");
    }
  }

  private boolean isDiscretionaryEligibility(EligibilityCodeField type) {
    return enumValues(WALKD, ARMS, CHILDVEHIC, CHILDBULK, TERMILL).contains(type);
  }
}
