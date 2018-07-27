package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static java.lang.Boolean.TRUE;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_ARMS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ValidationBase.ErrorTypes.NOT_VALID;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;

@Component
class ArmsValidator extends ValidationBase {
  void validate(Application app, Errors errors) {
    // Have enough data to validate without null pointers?
    if (notExists(errors, KEY_ELI_ARMS)) {
      return;
    }

    if (!TRUE.equals(app.getEligibility().getDisabilityArms().isIsAdaptedVehicle())
        && hasText(errors, KEY_ELI_ARMS_VEH_ADAPTION)) {

      errors.rejectValue(
          KEY_ELI_ARMS_VEH_ADAPTION,
          NOT_VALID,
          KEY_ELI_ARMS_VEH_ADAPTION + " cannot be entered if is adapted vehicle not true.");
    }
  }
}
