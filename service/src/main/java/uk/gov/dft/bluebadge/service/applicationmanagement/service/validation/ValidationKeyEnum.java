package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

public enum ValidationKeyEnum {
  MISSING_PERSON_OBJECT(
      "NotNull.party.person",
      "Person details must be included if party is a person.",
      "party.person"),
  MISSING_ORG_OBJECT(
      "NotNull.party.organisation",
      "Organisation details must be included if party is an organisation.",
      "party.organisation"),
  MISSING_ELIGIBILITY_OBJECT(
      "NotNull.eligibility",
      "Eligibility is required if application is for a person",
      "eligibility"),
  MISSING_BENEFIT_OBJECT(
      "NotNull.eligibility.benefit", "benefit required if PIP, DLA or WPMS", "eligibility.benefit"),
  MISSING_WALKING_OBJECT(
      "NotNull.eligibility.walkingDifficulty",
      "walkingDifficulty required if eligibility is WALKD",
      "eligibility.walkingDifficulty"),
  MISSING_ARMS_OBJECT(
      "NotNull.eligibility.disabilityArms",
      "disabilityArms required if eligibility ARMS",
      "eligibility.disabilityArms"),
  MISSING_CHILD_OBJECT(
      "NotNull.eligibility.childUnder3",
      "childUnder3 required if eligibility CHILDBULK",
      "eligibility.childUnder3"),
  INVALID_LA("Invalid.localAuthorityCode", "Not a valid localAuthorityCode", "localAuthorityCode");

  private final String key;
  private final String defaultMessage;
  private final String field;

  ValidationKeyEnum(String key, String defaultMessage, String field) {

    this.key = key;
    this.defaultMessage = defaultMessage;
    this.field = field;
  }

  public void addFieldError(Errors errors) {
    errors.rejectValue(field, key, defaultMessage);
  }

  public String getKey() {
    return key;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  public String getField() {
    return field;
  }
}
