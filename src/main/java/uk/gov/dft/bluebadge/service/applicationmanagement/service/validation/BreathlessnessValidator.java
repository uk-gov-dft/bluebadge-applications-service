package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BreathlessnessTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;

import static uk.gov.dft.bluebadge.common.util.Matchers.collection;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.*;

@Component
class BreathlessnessValidator extends AbstractValidator {

  void validate(Application app, Errors errors) {
    if (hasNoFieldErrors(errors, KEY_ELI_WALKING_BREATHLESSNESS)) {
      // If exists then validate values.

      // Must have at least 1 type code to continue
      if (collection(app.getEligibility().getWalkingDifficulty().getBreathlessness().getTypeCodes()).isNullOrEmpty()) {
        errors.rejectValue(
                KEY_ELI_WALKING_BREATHLESSNESS_TYPES,
            NOT_VALID,
            "Must have at least 1 breathlessness type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.");
        return;
      }

      validateBreathlessnessOtherDescription(app, errors);
    }
  }

  /**
   * Breathlessness type codes must contain OTHER if additional freetext entered.
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateBreathlessnessOtherDescription(Application app, Errors errors) {
    if ((!app.getEligibility()
            .getWalkingDifficulty()
            .getBreathlessness()
            .getTypeCodes()
            .contains(BreathlessnessTypeCodeField.OTHER))
        && hasText(app, KEY_ELI_BREATHLESSNESS_OTHER_DESC)) {
      // If breathlessness OTHER isn't selected cant have other description.
      errors.rejectValue(
              KEY_ELI_BREATHLESSNESS_OTHER_DESC,
          NOT_VALID,
              KEY_ELI_BREATHLESSNESS_OTHER_DESC + " can only be present if OTHER selected as a type.");
    }
  }
}
