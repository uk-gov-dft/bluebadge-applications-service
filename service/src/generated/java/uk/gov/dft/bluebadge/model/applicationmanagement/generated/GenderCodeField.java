package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets GenderCodeField */
public enum GenderCodeField {
  FEMALE("FEMALE"),

  MALE("MALE"),

  UNSPECIFIE("UNSPECIFIE");

  private String value;

  GenderCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static GenderCodeField fromValue(String text) {
    for (GenderCodeField b : GenderCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
