package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

public enum ValidationKeyEnum {
  MISSING_PARTY_OBJECT("NotNull.application.party", "Party cannot be null", "party"),
  MISSING_CONTACT_OBJECT("NotNull.application.party.contact", "Contact cannot be null", "contact"),
  MISSING_PERSON_OBJECT(
      "NotNull.application.party.person",
      "Person details must be included if party is a person.",
      "person"),
  MISSING_ORG_OBJECT(
      "NotNull.application.party.organisation",
      "Organisation details must be included if party is an organisation.",
      "organisation");

  private final String key;
  private final String defaultMessage;
  private final String field;

  ValidationKeyEnum(String key, String defaultMessage, String field) {

    this.key = key;
    this.defaultMessage = defaultMessage;
    this.field = field;
  }

  public ErrorErrors getFieldErrorInstance() {
    ErrorErrors error = new ErrorErrors();
    error.setField(field);
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }

  public Error getSystemErrorInstance() {
    Error error = new Error();
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }
}
