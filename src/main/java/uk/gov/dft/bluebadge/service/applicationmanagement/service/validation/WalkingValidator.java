package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.common.util.Matchers.collection;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALKING;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_OTHER_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_SPEED;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_WALK_TYPES;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;

@Component
class WalkingValidator extends AbstractValidator {

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

      validateWalkingOtherDescription(app, errors);
      validateWalkingSpeed(app, errors);
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
  void validateWalkingOtherDescription(Application app, Errors errors) {
    if (!app.getEligibility()
            .getWalkingDifficulty()
            .getTypeCodes()
            .contains(WalkingDifficultyTypeCodeField.SOMELSE)
        && hasText(app, KEY_ELI_WALK_OTHER_DESC)) {
      // If walking difficulty does not have something else selected cant have other
      // description.
      errors.rejectValue(
          KEY_ELI_WALK_OTHER_DESC,
          NOT_VALID,
          KEY_ELI_WALK_OTHER_DESC + " can only be present if SOMELSE selected as a type.");
    }
  }
}
