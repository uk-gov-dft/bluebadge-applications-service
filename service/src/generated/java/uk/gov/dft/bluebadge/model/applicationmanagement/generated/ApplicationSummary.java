package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** ApplicationSummary */
@Validated
public class ApplicationSummary {
  @JsonProperty("applicationId")
  private String applicationId = null;

  /** Gets or Sets partyTypeCode */
  public enum PartyTypeCodeEnum {
    PERSON("PERSON"),

    ORG("ORG");

    private String value;

    PartyTypeCodeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PartyTypeCodeEnum fromValue(String text) {
      for (PartyTypeCodeEnum b : PartyTypeCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("partyTypeCode")
  private PartyTypeCodeEnum partyTypeCode = null;

  @JsonProperty("applicationTypeCode")
  private String applicationTypeCode = null;

  @JsonProperty("nino")
  private String nino = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("submissionDate")
  private OffsetDateTime submissionDate = null;

  @JsonProperty("eligibilityCode")
  private String eligibilityCode = null;

  public ApplicationSummary applicationId(String applicationId) {
    this.applicationId = applicationId;
    return this;
  }

  /**
   * The unique badge number for this application - a UUID
   *
   * @return applicationId
   */
  @ApiModelProperty(
    example = "12345678-1234-1234-1234-123412341234",
    value = "The unique badge number for this application - a UUID"
  )
  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public ApplicationSummary partyTypeCode(PartyTypeCodeEnum partyTypeCode) {
    this.partyTypeCode = partyTypeCode;
    return this;
  }

  /**
   * Get partyTypeCode
   *
   * @return partyTypeCode
   */
  @ApiModelProperty(example = "PERSON", value = "")
  @Size(max = 10)
  public PartyTypeCodeEnum getPartyTypeCode() {
    return partyTypeCode;
  }

  public void setPartyTypeCode(PartyTypeCodeEnum partyTypeCode) {
    this.partyTypeCode = partyTypeCode;
  }

  public ApplicationSummary applicationTypeCode(String applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
    return this;
  }

  /**
   * NEW, CANCEL, RENEW, REPLACE
   *
   * @return applicationTypeCode
   */
  @ApiModelProperty(example = "NEW", value = "NEW, CANCEL, RENEW, REPLACE")
  @Size(max = 10)
  public String getApplicationTypeCode() {
    return applicationTypeCode;
  }

  public void setApplicationTypeCode(String applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
  }

  public ApplicationSummary nino(String nino) {
    this.nino = nino;
    return this;
  }

  /**
   * The badgeholders national insurance number
   *
   * @return nino
   */
  @ApiModelProperty(example = "NS123458S", value = "The badgeholders national insurance number")
  public String getNino() {
    return nino;
  }

  public void setNino(String nino) {
    this.nino = nino;
  }

  public ApplicationSummary name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the badge holder Organisation or Person
   *
   * @return name
   */
  @ApiModelProperty(
    example = "John Smith",
    value = "The name of the badge holder Organisation or Person"
  )
  @Size(max = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ApplicationSummary submissionDate(OffsetDateTime submissionDate) {
    this.submissionDate = submissionDate;
    return this;
  }

  /**
   * Submitted date and time
   *
   * @return submissionDate
   */
  @ApiModelProperty(example = "2018-12-25T12:30:45Z", value = "Submitted date and time")
  @Valid
  public OffsetDateTime getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(OffsetDateTime submissionDate) {
    this.submissionDate = submissionDate;
  }

  public ApplicationSummary eligibilityCode(String eligibilityCode) {
    this.eligibilityCode = eligibilityCode;
    return this;
  }

  /**
   * Get eligibilityCode
   *
   * @return eligibilityCode
   */
  @ApiModelProperty(example = "PIP", value = "")
  @Size(max = 10)
  public String getEligibilityCode() {
    return eligibilityCode;
  }

  public void setEligibilityCode(String eligibilityCode) {
    this.eligibilityCode = eligibilityCode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationSummary applicationSummary = (ApplicationSummary) o;
    return Objects.equals(this.applicationId, applicationSummary.applicationId)
        && Objects.equals(this.partyTypeCode, applicationSummary.partyTypeCode)
        && Objects.equals(this.applicationTypeCode, applicationSummary.applicationTypeCode)
        && Objects.equals(this.nino, applicationSummary.nino)
        && Objects.equals(this.name, applicationSummary.name)
        && Objects.equals(this.submissionDate, applicationSummary.submissionDate)
        && Objects.equals(this.eligibilityCode, applicationSummary.eligibilityCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        applicationId,
        partyTypeCode,
        applicationTypeCode,
        nino,
        name,
        submissionDate,
        eligibilityCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationSummary {\n");

    sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
    sb.append("    partyTypeCode: ").append(toIndentedString(partyTypeCode)).append("\n");
    sb.append("    applicationTypeCode: ")
        .append(toIndentedString(applicationTypeCode))
        .append("\n");
    sb.append("    nino: ").append(toIndentedString(nino)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    submissionDate: ").append(toIndentedString(submissionDate)).append("\n");
    sb.append("    eligibilityCode: ").append(toIndentedString(eligibilityCode)).append("\n");
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
