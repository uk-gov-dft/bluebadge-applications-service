package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BLIND;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BLIND_REG_AT_LA;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

@Component
public class BlindValidator extends AbstractValidator {

  private final ReferenceDataService referenceDataService;

  @Autowired
  BlindValidator(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  public void validate(Application app, Errors errors) {
    if (exists(app, KEY_ELI_BLIND)
        && exists(app, KEY_ELI_BLIND_REG_AT_LA)
        && StringUtils.stripToNull(app.getEligibility().getBlind().getRegisteredAtLaId()) != null
        && !referenceDataService.isAuthorityCodeValid(
            app.getEligibility().getBlind().getRegisteredAtLaId())) {
      errors.rejectValue(KEY_ELI_BLIND_REG_AT_LA, NOT_VALID, "Invalid local authority code.");
    }
  }
}
