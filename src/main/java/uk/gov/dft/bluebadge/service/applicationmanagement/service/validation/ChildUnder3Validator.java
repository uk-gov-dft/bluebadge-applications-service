package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BLIND;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_BLIND_REG_AT_LA;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3_OTHER_DESC;

@Component
public class ChildUnder3Validator extends AbstractValidator {

  private final ReferenceDataService referenceDataService;

  @Autowired
  ChildUnder3Validator(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  public void validate(Application app, Errors errors) {

    if (!exists(app, KEY_ELI_CHILD3)) {
      errors.rejectValue(KEY_ELI_CHILD3, NOT_VALID, "Must be specified.");
      return;
    }

    if (BulkyMedicalEquipmentTypeCodeField.OTHER == app.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode() &&
        StringUtils.isBlank(app.getEligibility().getChildUnder3().getOtherMedicalEquipment())) {
      rejectIfEmptyOrWhitespace(errors,KEY_ELI_CHILD3_OTHER_DESC, "Must be specified");
    }
  }
}
