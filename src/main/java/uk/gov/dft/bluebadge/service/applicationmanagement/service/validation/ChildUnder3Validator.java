package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3_OTHER_DESC;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELI_CHILD3_TYPES;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ChildUnder3;

@Component
public class ChildUnder3Validator extends AbstractValidator {

  public void validate(Application app, Errors errors) {

    if (!exists(app, KEY_ELI_CHILD3)) {
      errors.rejectValue(KEY_ELI_CHILD3, NOT_VALID, "Must be specified.");
      return;
    }

    ChildUnder3 childUnder3 = app.getEligibility().getChildUnder3();

    // Following test is because of deprecated non list version of type code.
    // When deprecation complete, set not null on bean and just check size.
    if (null == childUnder3.getBulkyMedicalEquipmentTypeCode()
        && (null == childUnder3.getBulkyMedicalEquipmentTypeCodes()
            || childUnder3.getBulkyMedicalEquipmentTypeCodes().isEmpty())) {
      errors.rejectValue(
          KEY_ELI_CHILD3_TYPES,
          "NotNull",
          "At least 1 bulky medical equipment type code must be specified");

      // No further validation if no type code.
      return;
    }

    // Non list version of type code is deprecated.  Following can be
    // simplified/removed...eventually
    boolean hasOtherType =
        BulkyMedicalEquipmentTypeCodeField.OTHER == childUnder3.getBulkyMedicalEquipmentTypeCode()
            || (null != childUnder3.getBulkyMedicalEquipmentTypeCodes()
                && childUnder3
                    .getBulkyMedicalEquipmentTypeCodes()
                    .contains(BulkyMedicalEquipmentTypeCodeField.OTHER));

    if (hasOtherType && StringUtils.isBlank(childUnder3.getOtherMedicalEquipment())) {
      rejectIfEmptyOrWhitespace(
          errors,
          KEY_ELI_CHILD3_OTHER_DESC,
          "Must be specified if a bulky equipment type of OTHER is present");
    }

    // validate NOT OTHER and otherDescription supplied
    if (!hasOtherType && StringUtils.isNotBlank(childUnder3.getOtherMedicalEquipment())) {
      errors.rejectValue(
          KEY_ELI_CHILD3_OTHER_DESC,
          NOT_VALID,
          null,
          "Can only be supplied if a bulky equipment type of OTHER is present");
    }
    // End of deprecation
  }
}
