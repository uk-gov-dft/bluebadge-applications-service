package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

/** Breathlessness */
@Validated
public class Breathlessness {
  @JsonProperty("typeCodes")
  private List<BreathlessnessTypeCodeField> typeCodes = null;

  @JsonProperty("otherDescription")
  private String otherDescription = null;

  public Breathlessness typeCodes(List<BreathlessnessTypeCodeField> typeCodes) {
    this.typeCodes = typeCodes;
    return this;
  }

  public Breathlessness otherDescription(String otherDescription) {
    this.otherDescription = otherDescription;
    return this;
  }
  /**
   * Get typeCodes
   *
   * @return typeCodes
   */
  @ApiModelProperty(required = true, value = "")
  @NotEmpty
  @Valid
  public List<BreathlessnessTypeCodeField> getTypeCodes() {
    return typeCodes;
  }

  public void setTypeCodes(List<BreathlessnessTypeCodeField> typeCodes) {
    this.typeCodes = typeCodes;
  }

  /**
   * Get otherDescription
   *
   * @return otherDescription
   */
  @ApiModelProperty(required = false, value = "")
  @Size(max = 255)
  public String getOtherDescription() {
    return otherDescription;
  }

  public void setOtherDescription(String otherDescription) {
    this.otherDescription = otherDescription;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Breathlessness breathlessness = (Breathlessness) o;
    return Objects.equals(this.typeCodes, breathlessness.typeCodes)
        && Objects.equals(this.otherDescription, breathlessness.otherDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeCodes, otherDescription);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("typeCodes", typeCodes)
        .append("otherDescription", otherDescription)
        .toString();
  }
}
