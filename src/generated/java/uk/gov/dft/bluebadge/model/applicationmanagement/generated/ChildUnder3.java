package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/** ChildUnder3 */

/**
 * bulkyMedicalEquipmentTypeCode is DEPRECATED - use the array version. Until removed One entry will
 * be put in the non array field by citizen application if there are multiple codes entered. All
 * entered values will be in the array version.
 */
@ApiModel(
  description =
      "bulkyMedicalEquipmentTypeCode is DEPRECATED - use the array version.  Until removed One entry will be put in the non array field by citizen application if there are multiple codes entered. All entered values will be in the array version."
)
@Validated
public class ChildUnder3 {
  @Deprecated
  @JsonProperty("bulkyMedicalEquipmentTypeCode")
  private BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode = null;

  @JsonProperty("bulkyMedicalEquipmentTypeCodes")
  @Valid
  private List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes = null;

  @JsonProperty("otherMedicalEquipment")
  private String otherMedicalEquipment = null;

  public ChildUnder3 bulkyMedicalEquipmentTypeCode(
      BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode) {
    this.bulkyMedicalEquipmentTypeCode = bulkyMedicalEquipmentTypeCode;
    return this;
  }

  /**
   * Get bulkyMedicalEquipmentTypeCode
   *
   * @return bulkyMedicalEquipmentTypeCode
   */
  @ApiModelProperty(required = true, value = "")
  public BulkyMedicalEquipmentTypeCodeField getBulkyMedicalEquipmentTypeCode() {
    return bulkyMedicalEquipmentTypeCode;
  }

  public void setBulkyMedicalEquipmentTypeCode(
      BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode) {
    this.bulkyMedicalEquipmentTypeCode = bulkyMedicalEquipmentTypeCode;
  }

  public ChildUnder3 bulkyMedicalEquipmentTypeCodes(
      List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes) {
    this.bulkyMedicalEquipmentTypeCodes = bulkyMedicalEquipmentTypeCodes;
    return this;
  }

  public ChildUnder3 addBulkyMedicalEquipmentTypeCodesItem(
      BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCodesItem) {
    if (this.bulkyMedicalEquipmentTypeCodes == null) {
      this.bulkyMedicalEquipmentTypeCodes = new ArrayList<>();
    }
    this.bulkyMedicalEquipmentTypeCodes.add(bulkyMedicalEquipmentTypeCodesItem);
    return this;
  }

  /**
   * Get bulkyMedicalEquipmentTypeCodes
   *
   * @return bulkyMedicalEquipmentTypeCodes
   */
  @ApiModelProperty(value = "")
  // BBB-1093 Will be not null when deprecated version removed.
  //@NotNull
  //@Valid
  public List<BulkyMedicalEquipmentTypeCodeField> getBulkyMedicalEquipmentTypeCodes() {
    return bulkyMedicalEquipmentTypeCodes;
  }

  public void setBulkyMedicalEquipmentTypeCodes(
      List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes) {
    this.bulkyMedicalEquipmentTypeCodes = bulkyMedicalEquipmentTypeCodes;
  }

  public ChildUnder3 otherMedicalEquipment(String otherMedicalEquipment) {
    this.otherMedicalEquipment = otherMedicalEquipment;
    return this;
  }

  /**
   * A short description of the medical equipment.
   *
   * @return otherMedicalEquipment
   */
  @ApiModelProperty(
    example = "Oxygen cylinders",
    value = "A short description of the medical equipment."
  )
  @Size(max = 100)
  public String getOtherMedicalEquipment() {
    return otherMedicalEquipment;
  }

  public void setOtherMedicalEquipment(String otherMedicalEquipment) {
    this.otherMedicalEquipment = otherMedicalEquipment;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChildUnder3 childUnder3 = (ChildUnder3) o;
    return Objects.equals(
            this.bulkyMedicalEquipmentTypeCodes, childUnder3.bulkyMedicalEquipmentTypeCodes)
        && Objects.equals(this.otherMedicalEquipment, childUnder3.otherMedicalEquipment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bulkyMedicalEquipmentTypeCodes, otherMedicalEquipment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChildUnder3 {\n");

    sb.append("    bulkyMedicalEquipmentTypeCodes: ")
        .append(toIndentedString(bulkyMedicalEquipmentTypeCodes))
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
