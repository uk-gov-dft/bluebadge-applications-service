package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.*;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BreathlessnessTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;

@Component
class BreathlessnessValidator extends AbstractValidator {

  void validate(Application app, Errors errors) {
    if (hasNoFieldErrors(errors, KEY_ELI_WALKING_BREATHLESSNESS)) {
      WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();

      if (null == walkingDifficulty) {
        errors.rejectValue(KEY_ELI_WALKING, NOT_VALID, "Eligibility must be set to WALKDIFF");
        return;
      }

      // Must set WalkingDifficulty type to BREATH if want to use BREATHLESSNESS types
      if (!walkingDifficulty.getTypeCodes().contains(WalkingDifficultyTypeCodeField.BREATH)) {
        if (null != walkingDifficulty.getBreathlessness()) {
          errors.rejectValue(
              KEY_ELI_WALKING_BREATHLESSNESS_TYPES,
              NOT_VALID,
              "For BREATHLESSNESS you must select BREATH as on of the Walking difficulty types");
          return;
        }
      } else {
        if (null == walkingDifficulty.getBreathlessness()) {
          errors.rejectValue(
              KEY_ELI_WALKING_BREATHLESSNESS_TYPES,
              NOT_VALID,
              "Must have at least 1 BREATHLESSNESS type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.\"");
          return;
        } else {
          validateBreathlessnessOtherDescription(app, errors);
        }
      }

      return;
    }
  }

  /**
   * Breathlessness type codes must contain OTHER if additional freetext entered.
   *
   * @param app Application.
   * @param errors Errors.
   */
  void validateBreathlessnessOtherDescription(Application app, Errors errors) {
    WalkingDifficulty walkingDifficulty = app.getEligibility().getWalkingDifficulty();

    if ((!walkingDifficulty
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

    if ((walkingDifficulty
            .getBreathlessness()
            .getTypeCodes()
            .contains(BreathlessnessTypeCodeField.OTHER))
        && !hasText(app, KEY_ELI_BREATHLESSNESS_OTHER_DESC)) {
      // If breathlessness OTHER is selected must have other description.
      errors.rejectValue(
          KEY_ELI_BREATHLESSNESS_OTHER_DESC,
          NOT_VALID,
          KEY_ELI_BREATHLESSNESS_OTHER_DESC + " must be present if OTHER selected as a type.");
    }
  }
}
