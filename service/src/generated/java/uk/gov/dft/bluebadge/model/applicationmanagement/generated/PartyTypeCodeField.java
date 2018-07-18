package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets PartyTypeCodeField */
public enum PartyTypeCodeField {
  PERSON("PERSON"),

  ORG("ORG");

  private String value;

  PartyTypeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PartyTypeCodeField fromValue(String text) {
    for (PartyTypeCodeField b : PartyTypeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
