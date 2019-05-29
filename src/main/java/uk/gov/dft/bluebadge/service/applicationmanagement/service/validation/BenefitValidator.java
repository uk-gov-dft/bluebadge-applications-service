package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static java.lang.Boolean.TRUE;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_NULL;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENEFIT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENE_EXPIRY_DT;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BENE_IS_INDEFINITE;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;

@Component
class BenefitValidator extends AbstractValidator {

  void validate(Application app, Errors errors) {
    if (exists(app, KEY_ELI_BENEFIT)) {
      if (!app.isRenewal()) {
        rejectIfEmptyOrWhitespace(errors, KEY_ELI_BENE_IS_INDEFINITE, NOT_NULL);
      }
      if (exists(app, KEY_ELI_BENE_EXPIRY_DT) && exists(app, KEY_ELI_BENE_IS_INDEFINITE)) {
        if (LocalDate.now().isAfter(app.getEligibility().getBenefit().getExpiryDate())) {
          errors.rejectValue(
              KEY_ELI_BENE_EXPIRY_DT, NOT_VALID, "Benefit expiry date cannot be in the past.");
        }

        if (TRUE.equals(app.getEligibility().getBenefit().getIsIndefinite())) {
          errors.rejectValue(
              KEY_ELI_BENE_EXPIRY_DT,
              NOT_VALID,
              "Benefit expiry date cannot be entered if benefit is indefinite.");
        }
      }
    }
  }
}
