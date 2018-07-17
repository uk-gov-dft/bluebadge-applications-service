package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** Person */
@Validated
public class Person {
  @JsonProperty("badgeHolderName")
  private String badgeHolderName = null;

  @JsonProperty("nino")
  private String nino = null;

  @JsonProperty("dob")
  private LocalDate dob = null;

  @JsonProperty("nameAtBirth")
  private String nameAtBirth = null;

  @JsonProperty("genderCode")
  private String genderCode = null;

  public Person badgeHolderName(String badgeHolderName) {
    this.badgeHolderName = badgeHolderName;
    return this;
  }

  /**
   * Get badgeHolderName
   *
   * @return badgeHolderName
   */
  @ApiModelProperty(example = "John Smith", required = true, value = "")
  @NotNull
  @Size(max = 100)
  public String getBadgeHolderName() {
    return badgeHolderName;
  }

  public void setBadgeHolderName(String badgeHolderName) {
    this.badgeHolderName = badgeHolderName;
  }

  public Person nino(String nino) {
    this.nino = nino;
    return this;
  }

  /**
   * The badgeholders national insurance number
   *
   * @return nino
   */
  @ApiModelProperty(example = "NS123456A", value = "The badgeholders national insurance number")
  public String getNino() {
    return nino;
  }

  public void setNino(String nino) {
    this.nino = nino;
  }

  public Person dob(LocalDate dob) {
    this.dob = dob;
    return this;
  }

  /**
   * Date of birth YYYY-MM-DD
   *
   * @return dob
   */
  @ApiModelProperty(example = "1970-05-29", value = "Date of birth YYYY-MM-DD")
  @Valid
  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public Person nameAtBirth(String nameAtBirth) {
    this.nameAtBirth = nameAtBirth;
    return this;
  }

  /**
   * Get nameAtBirth
   *
   * @return nameAtBirth
   */
  @ApiModelProperty(example = "John Smith", value = "")
  @Size(max = 100)
  public String getNameAtBirth() {
    return nameAtBirth;
  }

  public void setNameAtBirth(String nameAtBirth) {
    this.nameAtBirth = nameAtBirth;
  }

  public Person genderCode(String genderCode) {
    this.genderCode = genderCode;
    return this;
  }

  /**
   * Reference data code MALE or FEMALE
   *
   * @return genderCode
   */
  @ApiModelProperty(example = "MALE", value = "Reference data code MALE or FEMALE")
  @Size(max = 10)
  public String getGenderCode() {
    return genderCode;
  }

  public void setGenderCode(String genderCode) {
    this.genderCode = genderCode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals(this.badgeHolderName, person.badgeHolderName)
        && Objects.equals(this.nino, person.nino)
        && Objects.equals(this.dob, person.dob)
        && Objects.equals(this.nameAtBirth, person.nameAtBirth)
        && Objects.equals(this.genderCode, person.genderCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badgeHolderName, nino, dob, nameAtBirth, genderCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Person {\n");

    sb.append("    badgeHolderName: ").append(toIndentedString(badgeHolderName)).append("\n");
    sb.append("    nino: ").append(toIndentedString(nino)).append("\n");
    sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
    sb.append("    nameAtBirth: ").append(toIndentedString(nameAtBirth)).append("\n");
    sb.append("    genderCode: ").append(toIndentedString(genderCode)).append("\n");
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
