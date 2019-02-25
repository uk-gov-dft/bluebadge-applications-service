package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonValue;

/** Gets or Sets ApplicationTypeCodeField */
public enum ApplicationStatusField {
  TODO("TODO"),
  INPROGRESS("INPROGRESS"),
  COMPLETED("COMPLETED");

  private String value;

  ApplicationStatusField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  //  @JsonCreator
  public static ApplicationStatusField fromValue(String text) {
    for (ApplicationStatusField b : ApplicationStatusField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
