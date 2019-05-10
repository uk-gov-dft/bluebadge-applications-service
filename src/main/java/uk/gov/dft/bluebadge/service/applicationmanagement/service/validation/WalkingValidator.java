package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.common.util.Matchers.collection;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_NULL;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALKING;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_BALANCE_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_BALANCE_FALLS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_DANGER_CONDITIONS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_DANGER_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_OTHER_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_PAIN_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_SPEED;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_TYPES;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;

@Component
class WalkingValidator extends AbstractValidator {

  private BreathlessnessValidator breathlessnessValidator;

  @Autowired
  public WalkingValidator(BreathlessnessValidator breathlessnessValidator) {
    this.breathlessnessValidator = breathlessnessValidator;
  }

  void validate(Application app, Errors errors) {
    if (hasNoFieldErrors(errors, KEY_ELI_WALKING)) {
      // If exists then validate values.

      // Must have at least 1 type code to continue
      if (collection(app.getEligibility().getWalkingDifficulty().getTypeCodes()).isNullOrEmpty()) {
        errors.rejectValue(
            KEY_ELI_WALK_TYPES,
            NOT_VALID,
            "Must have at least 1 walking type code if eligibility is WALKDIFF.");
        return;
      }

      validateWalkingDifficulties(app, errors);
      validateWalkingSpeed(app, errors);
      validateBreathlessness(app, errors);
    }
  }

  /**
   * Walking speed only present if can walk, from walking length of time.
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateWalkingSpeed(Application app, Errors errors) {
    // Walking time code is mandatory for walking
    Assert.notNull(
        app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode(),
        "If WALKD then time code should be not null");

    if (CANTWALK.equals(app.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode())
        && exists(app, KEY_ELI_WALK_SPEED)) {
      errors.rejectValue(
          KEY_ELI_WALK_SPEED,
          NOT_VALID,
          KEY_ELI_WALK_SPEED + " can only be present if walking length of time is not can't walk.");
    }
  }

  /**
   * Walking type codes must contain SOMELSE if additional freetext entered.
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateWalkingDifficulties(Application app, Errors errors) {
    List<WalkingDifficultyTypeCodeField> typeCodes =
        app.getEligibility().getWalkingDifficulty().getTypeCodes();

    if (typeCodes.contains(WalkingDifficultyTypeCodeField.PAIN)) {
      if (!hasText(app, KEY_ELI_WALK_PAIN_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_PAIN_DESC,
            NOT_NULL,
            KEY_ELI_WALK_PAIN_DESC + " must be present if PAIN selected as a type.");
      }
    } else if (hasText(app, KEY_ELI_WALK_PAIN_DESC)) {
      errors.rejectValue(
          KEY_ELI_WALK_PAIN_DESC,
          NOT_VALID,
          KEY_ELI_WALK_PAIN_DESC + " can only be present if PAIN selected as a type.");
    }

    if (typeCodes.contains(WalkingDifficultyTypeCodeField.BALANCE)) {
      if (!hasText(app, KEY_ELI_WALK_BALANCE_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_BALANCE_DESC,
            NOT_NULL,
            KEY_ELI_WALK_BALANCE_DESC + " must be present if BALANCE selected as a type.");
      }
      if (notExists(app, KEY_ELI_WALK_BALANCE_FALLS)) {
        errors.rejectValue(
            KEY_ELI_WALK_BALANCE_FALLS,
            NOT_NULL,
            KEY_ELI_WALK_BALANCE_FALLS + " must be present if BALANCE selected as a type.");
      }
    } else {
      if (hasText(app, KEY_ELI_WALK_BALANCE_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_BALANCE_DESC,
            NOT_VALID,
            KEY_ELI_WALK_BALANCE_DESC + " can only be present if BALANCE selected as a type.");
      }
      if (exists(app, KEY_ELI_WALK_BALANCE_FALLS)) {
        errors.rejectValue(
            KEY_ELI_WALK_BALANCE_FALLS,
            NOT_VALID,
            KEY_ELI_WALK_BALANCE_FALLS + " can only be present if BALANCE selected as a type.");
      }
    }

    if (typeCodes.contains(WalkingDifficultyTypeCodeField.DANGER)) {
      if (!hasText(app, KEY_ELI_WALK_DANGER_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_DANGER_DESC,
            NOT_NULL,
            KEY_ELI_WALK_DANGER_DESC + " must be present if DANGER selected as a type.");
      }
      if (notExists(app, KEY_ELI_WALK_DANGER_CONDITIONS)) {
        errors.rejectValue(
            KEY_ELI_WALK_DANGER_CONDITIONS,
            NOT_NULL,
            KEY_ELI_WALK_DANGER_CONDITIONS + " must be present if DANGER selected as a type.");
      }
    } else {
      if (hasText(app, KEY_ELI_WALK_DANGER_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_DANGER_DESC,
            NOT_VALID,
            KEY_ELI_WALK_DANGER_DESC + " can only be present if DANGER selected as a type.");
      }
      if (exists(app, KEY_ELI_WALK_DANGER_CONDITIONS)) {
        errors.rejectValue(
            KEY_ELI_WALK_DANGER_CONDITIONS,
            NOT_VALID,
            KEY_ELI_WALK_DANGER_CONDITIONS + " can only be present if DANGER selected as a type.");
      }
    }

    if (typeCodes.contains(WalkingDifficultyTypeCodeField.SOMELSE)) {
      if (!hasText(app, KEY_ELI_WALK_OTHER_DESC)) {
        errors.rejectValue(
            KEY_ELI_WALK_OTHER_DESC,
            NOT_NULL,
            KEY_ELI_WALK_OTHER_DESC + " must be present if SOMELSE selected as a type.");
      }
    } else if (hasText(app, KEY_ELI_WALK_OTHER_DESC)) {
      errors.rejectValue(
          KEY_ELI_WALK_OTHER_DESC,
          NOT_VALID,
          KEY_ELI_WALK_OTHER_DESC + " can only be present if SOMELSE selected as a type.");
    }
  }

  /**
   * Breathlessness must be set if BREATH typeCode is selected; otherDescription must be set if
   * Breathlessness OTHER typeCode is selected
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateBreathlessness(Application app, Errors errors) {
    breathlessnessValidator.validate(app, errors);
  }
}
