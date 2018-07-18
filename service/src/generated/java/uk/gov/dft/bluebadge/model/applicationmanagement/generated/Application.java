package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** Application */
@Validated
public class Application {
  @JsonProperty("applicationId")
  private String applicationId = null;

  @JsonProperty("applicationTypeCode")
  private String applicationTypeCode = null;

  @JsonProperty("localAuthorityCode")
  private Integer localAuthorityCode = null;

  @JsonProperty("isPaymentTaken")
  private Boolean isPaymentTaken = null;

  @JsonProperty("submissionDate")
  private OffsetDateTime submissionDate = null;

  @JsonProperty("existingBadgeNumber")
  private String existingBadgeNumber = null;

  @JsonProperty("party")
  private Party party = null;

  @JsonProperty("eligibility")
  private Eligibility eligibility = null;

  @JsonProperty("artifacts")
  private Artifacts artifacts = null;

  public Application applicationId(String applicationId) {
    this.applicationId = applicationId;
    return this;
  }

  /**
   * The unique number for this application - a UUID
   *
   * @return applicationId
   */
  @ApiModelProperty(
    example = "12345678-1234-1234-1234-123456781234",
    value = "The unique number for this application - a UUID"
  )
  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public Application applicationTypeCode(String applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
    return this;
  }

  /**
   * NEW, CANCEL, RENEW, REPLACE
   *
   * @return applicationTypeCode
   */
  @ApiModelProperty(example = "NEW", required = true, value = "NEW, CANCEL, RENEW, REPLACE")
  @NotNull
  @Size(max = 10)
  public String getApplicationTypeCode() {
    return applicationTypeCode;
  }

  public void setApplicationTypeCode(String applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
  }

  public Application localAuthorityCode(Integer localAuthorityCode) {
    this.localAuthorityCode = localAuthorityCode;
    return this;
  }

  /**
   * The code for the local authority.
   *
   * @return localAuthorityCode
   */
  @ApiModelProperty(example = "211", required = true, value = "The code for the local authority.")
  @NotNull
  public Integer getLocalAuthorityCode() {
    return localAuthorityCode;
  }

  public void setLocalAuthorityCode(Integer localAuthorityCode) {
    this.localAuthorityCode = localAuthorityCode;
  }

  public Application isPaymentTaken(Boolean isPaymentTaken) {
    this.isPaymentTaken = isPaymentTaken;
    return this;
  }

  /**
   * Get isPaymentTaken
   *
   * @return isPaymentTaken
   */
  @ApiModelProperty(example = "true", required = true, value = "")
  @NotNull
  public Boolean isIsPaymentTaken() {
    return isPaymentTaken;
  }

  public void setIsPaymentTaken(Boolean isPaymentTaken) {
    this.isPaymentTaken = isPaymentTaken;
  }

  public Application submissionDate(OffsetDateTime submissionDate) {
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

  public Application existingBadgeNumber(String existingBadgeNumber) {
    this.existingBadgeNumber = existingBadgeNumber;
    return this;
  }

  /**
   * Get existingBadgeNumber
   *
   * @return existingBadgeNumber
   */
  @ApiModelProperty(value = "")
  public String getExistingBadgeNumber() {
    return existingBadgeNumber;
  }

  public void setExistingBadgeNumber(String existingBadgeNumber) {
    this.existingBadgeNumber = existingBadgeNumber;
  }

  public Application party(Party party) {
    this.party = party;
    return this;
  }

  /**
   * Get party
   *
   * @return party
   */
  @ApiModelProperty(value = "")
  @Valid
  public Party getParty() {
    return party;
  }

  public void setParty(Party party) {
    this.party = party;
  }

  public Application eligibility(Eligibility eligibility) {
    this.eligibility = eligibility;
    return this;
  }

  /**
   * Get eligibility
   *
   * @return eligibility
   */
  @ApiModelProperty(value = "")
  @Valid
  public Eligibility getEligibility() {
    return eligibility;
  }

  public void setEligibility(Eligibility eligibility) {
    this.eligibility = eligibility;
  }

  public Application artifacts(Artifacts artifacts) {
    this.artifacts = artifacts;
    return this;
  }

  /**
   * Get artifacts
   *
   * @return artifacts
   */
  @ApiModelProperty(value = "")
  @Valid
  public Artifacts getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(Artifacts artifacts) {
    this.artifacts = artifacts;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Application application = (Application) o;
    return Objects.equals(this.applicationId, application.applicationId)
        && Objects.equals(this.applicationTypeCode, application.applicationTypeCode)
        && Objects.equals(this.localAuthorityCode, application.localAuthorityCode)
        && Objects.equals(this.isPaymentTaken, application.isPaymentTaken)
        && Objects.equals(this.submissionDate, application.submissionDate)
        && Objects.equals(this.existingBadgeNumber, application.existingBadgeNumber)
        && Objects.equals(this.party, application.party)
        && Objects.equals(this.eligibility, application.eligibility)
        && Objects.equals(this.artifacts, application.artifacts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        applicationId,
        applicationTypeCode,
        localAuthorityCode,
        isPaymentTaken,
        submissionDate,
        existingBadgeNumber,
        party,
        eligibility,
        artifacts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Application {\n");

    sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
    sb.append("    applicationTypeCode: ")
        .append(toIndentedString(applicationTypeCode))
        .append("\n");
    sb.append("    localAuthorityCode: ").append(toIndentedString(localAuthorityCode)).append("\n");
    sb.append("    isPaymentTaken: ").append(toIndentedString(isPaymentTaken)).append("\n");
    sb.append("    submissionDate: ").append(toIndentedString(submissionDate)).append("\n");
    sb.append("    existingBadgeNumber: ")
        .append(toIndentedString(existingBadgeNumber))
        .append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    eligibility: ").append(toIndentedString(eligibility)).append("\n");
    sb.append("    artifacts: ").append(toIndentedString(artifacts)).append("\n");
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
