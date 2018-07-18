package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets WalkingDifficultyTypeCodeField */
public enum WalkingDifficultyTypeCodeField {
  PAIN("PAIN"),

  BREATH("BREATH"),

  BALANCE("BALANCE"),

  DANGER("DANGER"),

  STRUGGLE("STRUGGLE"),

  SOMELSE("SOMELSE");

  private String value;

  WalkingDifficultyTypeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static WalkingDifficultyTypeCodeField fromValue(String text) {
    for (WalkingDifficultyTypeCodeField b : WalkingDifficultyTypeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
