package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ValidationKeyEnum.MISSING_ELIGIBILITY_OBJECT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

import javax.validation.constraints.NotNull;

@Component
public class ApplicationValidator implements Validator {

  private ReferenceDataService referenceDataService;

  interface FieldKeys {
    String ELIGIBILITY = "eligibility";
    String ELIGIBILITY_TYPE = "eligibility.typeCode";
    String LA = "localAuthorityCode";
  }

  @Autowired
  public ApplicationValidator(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Application.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Application app = (Application) target;
    // Don't bother if root objects missing. These are picked up by jcr303 validation.
    if (null == app.getParty() || null == app.getParty().getContact()) {
      return;
    }

    if (!referenceDataService.isAuthorityCodeValid(app.getLocalAuthorityCode())) {
      rejectField(errors, ValidationKeyEnum.INVALID_LA);
    }

    boolean isPerson = PartyTypeCodeField.PERSON.equals(app.getParty().getTypeCode());
    if (isPerson) {
      // Require Person and eligibility objects
      rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_PERSON_OBJECT);
      rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_ELIGIBILITY_OBJECT);
      validateEligibility(app, errors);
    } else {
      rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_ORG_OBJECT);
    }
  }

  private void validateEligibility(Application app, Errors errors) {
    // Validate if eligibility present and type ok.
    if (errors.getFieldErrorCount(FieldKeys.ELIGIBILITY) == 0
        && errors.getFieldErrorCount(FieldKeys.ELIGIBILITY_TYPE) == 0) {
      EligibilityCodeField eligibilityType =
          EligibilityCodeField.fromValue(
              errors.getFieldValue(FieldKeys.ELIGIBILITY_TYPE).toString());
      switch (eligibilityType) {
        case PIP:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_BENEFIT_OBJECT);
          break;
        case DLA:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_BENEFIT_OBJECT);
          break;
        case WPMS:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_BENEFIT_OBJECT);
          break;
        case ARMS:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_ARMS_OBJECT);
          break;
        case BLIND:
          break;
        case WALKD:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_WALKING_OBJECT);
          break;
        case AFRFCS:
          break;
        case TERMILL:
          break;
        case CHILDBULK:
          rejectIfEmptyOrWhitespace(errors, ValidationKeyEnum.MISSING_CHILD_OBJECT);
          break;
        case CHILDVEHIC:
          break;
      }
    }
  }

  /**
   * @param errors To add rejection message to
   * @param field Field to check.
   * @return true if rejected
   */
  private static void rejectIfEmptyOrWhitespace(Errors errors, ValidationKeyEnum field) {
    ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, field.getField(), field.getKey(), field.getDefaultMessage());
  }

  private static void rejectField(Errors errors, ValidationKeyEnum field) {
    errors.rejectValue(field.getField(), field.getKey(), field.getDefaultMessage());
  }
}
