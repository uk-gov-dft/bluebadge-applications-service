package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets WalkingLengthOfTimeCodeField */
public enum WalkingLengthOfTimeCodeField {
  CANTWALK("CANTWALK"),

  LESSMIN("LESSMIN"),

  FEWMIN("FEWMIN"),

  MORETEN("MORETEN");

  private String value;

  WalkingLengthOfTimeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static WalkingLengthOfTimeCodeField fromValue(String text) {
    for (WalkingLengthOfTimeCodeField b : WalkingLengthOfTimeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
