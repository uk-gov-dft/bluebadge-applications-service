package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_NULL;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.SHOULD_NOT_EXIST;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public abstract class AbstractValidator {

  class ErrorTypes {
    private ErrorTypes() {}

    static final String NOT_VALID = "NotValid";
    static final String SHOULD_NOT_EXIST = NOT_VALID;
    static final String NOT_NULL = "NotNull";
  }

  protected void rejectIfExists(Errors errors, String fieldKey, String messagePrefix) {
    if (exists(errors, fieldKey)) {
      errors.rejectValue(
          fieldKey, SHOULD_NOT_EXIST, messagePrefix + ": " + fieldKey + " should be null ");
    }
  }

  protected void rejectIfEmptyOrWhitespace(Errors errors, String fieldKey, String messagePrefix) {
    Assert.notNull(messagePrefix, "messagePrefix must be provided");
    Assert.notNull(fieldKey, "fieldKey must be provided");

    ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, fieldKey, NOT_NULL, messagePrefix + ":" + fieldKey + " cannot be null.");
  }

  protected boolean hasNoFieldErrors(Errors errors, String fieldKey) {
    return errors.getFieldErrorCount(fieldKey) == 0;
  }

  protected boolean hasFieldErrors(Errors errors, String fieldKey) {
    return !hasNoFieldErrors(errors, fieldKey);
  }

  protected boolean exists(Errors errors, String fieldKey) {
    Object value = errors.getFieldValue(fieldKey);
    return null != value;
  }

  protected boolean notExists(Errors errors, String fieldKey) {
    return !exists(errors, fieldKey);
  }

  protected boolean hasText(Errors errors, String fieldKey) {
    return !notExists(errors, fieldKey)
        && StringUtils.isNotEmpty(errors.getFieldValue(fieldKey).toString());
  }
}