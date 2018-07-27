package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static java.lang.Boolean.TRUE;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_VALID;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ARTIFACTS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ELIGIBILITY;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_LA;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ORGANISATION;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_ORG_CHARITY_NO;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_PARTY;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_PARTY_TYPE;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_PERSON;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.FieldKeys.KEY_PERSON_DOB;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

/** Validates an application assuming the bean validation has previously been performed. */
@Component
@Slf4j
public class ApplicationValidator extends AbstractValidator implements Validator {

  private final ReferenceDataService referenceDataService;
  private EligibilityValidator eligibilityValidator;

  @Autowired
  ApplicationValidator(
      ReferenceDataService referenceDataService, EligibilityValidator eligibilityValidator) {
    this.referenceDataService = referenceDataService;
    this.eligibilityValidator = eligibilityValidator;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Application.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Application app = (Application) target;

    validateLocalAuthority(app, errors);

    // Don't continue if basic objects previously invalid due to bean validation.
    if (!hasParty(errors)) {
      return;
    }

    boolean isPerson = PartyTypeCodeField.PERSON.equals(app.getParty().getTypeCode());
    if (isPerson) {
      validatePerson(app, errors);
      eligibilityValidator.validate(app, errors);
    } else {
      validateOrganisation(app, errors);
    }
  }

  /**
   * Check we have the minimum set of valid data to make further validation worthwhile.
   *
   * @param errors Spring Errors.
   * @return true if can continue validation.
   */
  boolean hasParty(Errors errors) {
    // If party validation failed then skip rest of validation - don't know which path to take.
    // This should only be possible if party was null, if invalid would have failed to deserialize
    return hasNoFieldErrors(errors, KEY_PARTY_TYPE) && hasNoFieldErrors(errors, KEY_PARTY);
  }

  void validateLocalAuthority(Application app, Errors errors) {
    if (!referenceDataService.isAuthorityCodeValid(app.getLocalAuthorityCode())) {
      errors.rejectValue(KEY_LA, NOT_VALID, "Invalid local authority code.");
    }
  }

  void validatePerson(Application app, Errors errors) {

    String messagePrefix = "When party is KEY_PERSON";

    // Require Person and eligibility objects
    rejectIfEmptyOrWhitespace(errors, KEY_PERSON, messagePrefix);
    rejectIfEmptyOrWhitespace(errors, KEY_ELIGIBILITY, messagePrefix);
    rejectIfExists(errors, KEY_ORGANISATION, messagePrefix);
    validateDob(app, errors);
  }

  void validateDob(Application app, Errors errors) {
    if (hasNoFieldErrors(errors, KEY_PERSON)
        && hasNoFieldErrors(errors, KEY_PERSON_DOB)
        && app.getParty().getPerson().getDob().isAfter(LocalDate.now())) {
      errors.rejectValue(KEY_PERSON_DOB, NOT_VALID, "Date of birth cannot be in future.");
    }
  }

  void validateOrganisation(Application app, Errors errors) {
    String messagePrefix = "When party is ORG";
    rejectIfEmptyOrWhitespace(errors, KEY_ORGANISATION, messagePrefix);
    rejectIfExists(errors, KEY_PERSON, messagePrefix);
    rejectIfExists(errors, KEY_ELIGIBILITY, messagePrefix);
    rejectIfExists(errors, KEY_ARTIFACTS, messagePrefix);
    validateCharity(app, errors);
  }

  void validateCharity(Application app, Errors errors) {
    if (hasNoFieldErrors(errors, KEY_ORGANISATION)
        && !TRUE.equals(app.getParty().getOrganisation().isIsCharity())
        && exists(errors, KEY_ORG_CHARITY_NO)) {
      errors.rejectValue(
          KEY_ORG_CHARITY_NO,
          NOT_VALID,
          "Charity number can only be present if organisation is a charity.");
    }
  }
}
