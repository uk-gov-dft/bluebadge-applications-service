package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets HowProvidedCodeField */
public enum HowProvidedCodeField {
  PRESCRIBE("PRESCRIBE"),

  PRIVATE("PRIVATE"),

  SOCIAL("SOCIAL");

  private String value;

  HowProvidedCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static HowProvidedCodeField fromValue(String text) {
    for (HowProvidedCodeField b : HowProvidedCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
