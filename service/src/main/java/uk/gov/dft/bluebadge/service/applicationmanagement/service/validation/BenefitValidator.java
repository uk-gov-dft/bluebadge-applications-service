package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static java.lang.Boolean.TRUE;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENEFIT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENE_EXPIRY_DT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ValidationBase.ErrorTypes.NOT_VALID;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;

@Component
class BenefitValidator extends ValidationBase {

  void validate(Application app, Errors errors) {
    if (exists(errors, KEY_ELI_BENEFIT) && exists(errors, KEY_ELI_BENE_EXPIRY_DT)) {
      if (LocalDate.now().isAfter(app.getEligibility().getBenefit().getExpiryDate())) {
        errors.rejectValue(
            KEY_ELI_BENE_EXPIRY_DT, NOT_VALID, "Benefit expiry date cannot be in the past.");
      }

      if (TRUE.equals(app.getEligibility().getBenefit().isIsIndefinite())) {
        errors.rejectValue(
            KEY_ELI_BENE_EXPIRY_DT,
            NOT_VALID,
            "Benefit expiry date cannot be entered if benefit is indefinite.");
      }
    }
  }
}
