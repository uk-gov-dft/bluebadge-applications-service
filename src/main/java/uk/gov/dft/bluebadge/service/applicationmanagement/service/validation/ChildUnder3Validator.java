package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3_OTHER_DESC;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;

@Component
public class ChildUnder3Validator extends AbstractValidator {

  public void validate(Application app, Errors errors) {

    if (!exists(app, KEY_ELI_CHILD3)) {
      errors.rejectValue(KEY_ELI_CHILD3, NOT_VALID, "Must be specified.");
      return;
    }

    // validate OTHER specified and no otherDescription supplied
    if (BulkyMedicalEquipmentTypeCodeField.OTHER
            == app.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode()
        && StringUtils.isBlank(app.getEligibility().getChildUnder3().getOtherMedicalEquipment())) {
      rejectIfEmptyOrWhitespace(errors, KEY_ELI_CHILD3_OTHER_DESC, "Must be specified if OTHER");
    }

    // validate NOT OTHER and otherDescription supplied
    if (BulkyMedicalEquipmentTypeCodeField.OTHER
            != app.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode()
        && StringUtils.isNotBlank(
            app.getEligibility().getChildUnder3().getOtherMedicalEquipment())) {
      errors.rejectValue(
          KEY_ELI_CHILD3_OTHER_DESC, NOT_VALID, null, "Can only be supplied with OTHER");
    }
  }
}
