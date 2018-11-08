package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.validation.annotation.Validated;

/** ChildUnder3 */
@Validated
public class ChildUnder3 {
  @JsonProperty("bulkyMedicalEquipmentTypeCode")
  private BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode = null;

  @JsonProperty("otherMedicalEquipment")
  private String otherMedicalEquipment;

  //  public ChildUnder3 bulkyMedicalEquipmentTypeCode(
  //      BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode) {
  //    this.bulkyMedicalEquipmentTypeCode = bulkyMedicalEquipmentTypeCode;
  //    return this;
  //  }

  /**
   * Get bulkyMedicalEquipmentTypeCode
   *
   * @return bulkyMedicalEquipmentTypeCode
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public BulkyMedicalEquipmentTypeCodeField getBulkyMedicalEquipmentTypeCode() {
    return bulkyMedicalEquipmentTypeCode;
  }

  public void setBulkyMedicalEquipmentTypeCode(
      BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode) {
    this.bulkyMedicalEquipmentTypeCode = bulkyMedicalEquipmentTypeCode;
  }

  @ApiModelProperty(allowEmptyValue = true)
  public String getOtherMedicalEquipment() {
    return otherMedicalEquipment;
  }

  public void setOtherMedicalEquipment(String otherMedicalEquipment) {
    this.otherMedicalEquipment = otherMedicalEquipment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ChildUnder3 that = (ChildUnder3) o;

    return new EqualsBuilder()
        .append(bulkyMedicalEquipmentTypeCode, that.bulkyMedicalEquipmentTypeCode)
        .append(otherMedicalEquipment, that.otherMedicalEquipment)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(bulkyMedicalEquipmentTypeCode)
        .append(otherMedicalEquipment)
        .toHashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChildUnder3 {\n");

    sb.append("    bulkyMedicalEquipmentTypeCode: ")
        .append(toIndentedString(bulkyMedicalEquipmentTypeCode))
        .append("\n");
    sb.append("    otherMedicalEquipment: ")
        .append(toIndentedString(otherMedicalEquipment))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
