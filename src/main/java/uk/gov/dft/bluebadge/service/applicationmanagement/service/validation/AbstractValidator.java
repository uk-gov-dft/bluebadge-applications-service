package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.NOT_NULL;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator.ErrorTypes.SHOULD_NOT_EXIST;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;

public abstract class AbstractValidator {

  class ErrorTypes {
    private ErrorTypes() {}

    static final String NOT_VALID = "NotValid";
    static final String SHOULD_NOT_EXIST = NOT_VALID;
    static final String NOT_NULL = "NotNull";
  }

  protected void rejectIfExists(
      Application application, Errors errors, String fieldKey, String messagePrefix) {
    if (exists(application, fieldKey)) {
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

  protected boolean exists(Application app, String fieldKey) {
    try {
      ExpressionParser parser = new SpelExpressionParser();
      Expression exp = parser.parseExpression(fieldKey);
      Object message = exp.getValue(app);
      return null != message;
    } catch (NullPointerException e) {
      return false;
    }
  }

  protected boolean notExists(Application app, String fieldKey) {
    return !exists(app, fieldKey);
  }

  protected boolean hasText(Application app, String fieldKey) {
    if (exists(app, fieldKey)) {
      ExpressionParser parser = new SpelExpressionParser();
      Expression exp = parser.parseExpression(fieldKey);
      String message = (String) exp.getValue(app);
      return StringUtils.isNotEmpty(message);
    }
    return false;
  }
}
