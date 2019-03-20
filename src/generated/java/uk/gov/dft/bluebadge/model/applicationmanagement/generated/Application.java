package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

/** Application */
@Validated
public class Application {
  @JsonProperty("applicationId")
  private String applicationId = null;

  @JsonProperty("applicationTypeCode")
  private ApplicationTypeCodeField applicationTypeCode = null;

  @JsonProperty("localAuthorityCode")
  private String localAuthorityCode = null;

  @JsonProperty("transferredLaFromCode")
  private String transferredLaFromCode = null;

  @JsonProperty("transferredFromLaDate")
  private OffsetDateTime transferredFromLaDate = null;

  @JsonProperty("paymentTaken")
  private Boolean paymentTaken = null;

  @JsonProperty("submissionDate")
  private OffsetDateTime submissionDate = null;

  @JsonProperty("existingBadgeNumber")
  private String existingBadgeNumber = null;

  @JsonProperty("party")
  private Party party = null;

  @JsonProperty("eligibility")
  private Eligibility eligibility = null;

  @JsonProperty("paymentReference")
  private String paymentReference = null;

  @JsonProperty("artifacts")
  @Valid
  private List<Artifact> artifacts = null;

  @JsonProperty("applicationStatus")
  private ApplicationStatusField applicationStatus;

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

  public Application applicationTypeCode(ApplicationTypeCodeField applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
    return this;
  }

  /**
   * Get applicationTypeCode
   *
   * @return applicationTypeCode
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public ApplicationTypeCodeField getApplicationTypeCode() {
    return applicationTypeCode;
  }

  public void setApplicationTypeCode(ApplicationTypeCodeField applicationTypeCode) {
    this.applicationTypeCode = applicationTypeCode;
  }

  public Application localAuthorityCode(String localAuthorityCode) {
    this.localAuthorityCode = localAuthorityCode;
    return this;
  }

  /**
   * The code for the local authority.
   *
   * @return localAuthorityCode
   */
  @NotNull
  public String getLocalAuthorityCode() {
    return localAuthorityCode;
  }

  public void setLocalAuthorityCode(String localAuthorityCode) {
    this.localAuthorityCode = localAuthorityCode;
  }

  public String getTransferredLaFromCode() {
    return transferredLaFromCode;
  }

  public void setTransferredLaFromCode(String transferredLaFromCode) {
    this.transferredLaFromCode = transferredLaFromCode;
  }

  /**
   * Local authority transfer date and time.
   *
   * @return transferredFromLaDate
   */
  @ApiModelProperty(
    example = "2019-03-03T10:30:00Z",
    value = "Local authority transfer date and time."
  )
  @Valid
  public OffsetDateTime getTransferredFromLaDate() {
    return transferredFromLaDate;
  }

  public void setTransferredFromLaDate(OffsetDateTime transferredFromLaDate) {
    this.transferredFromLaDate = transferredFromLaDate;
  }

  public Application paymentTaken(Boolean paymentTaken) {
    this.paymentTaken = paymentTaken;
    return this;
  }

  /**
   * Get paymentTaken
   *
   * @return paymentTaken
   */
  @ApiModelProperty(example = "true", required = true, value = "")
  @NotNull
  public Boolean getPaymentTaken() {
    return paymentTaken;
  }

  public void setPaymentTaken(Boolean paymentTaken) {
    this.paymentTaken = paymentTaken;
  }

  public Application submissionDate(OffsetDateTime submissionDate) {
    this.submissionDate = submissionDate;
    return this;
  }

  /**
   * Submitted date and time. Populated automatically on create
   *
   * @return submissionDate
   */
  @ApiModelProperty(
    example = "2018-12-25T12:30:45Z",
    value = "Submitted date and time. Populated automatically on create"
  )
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
  @ApiModelProperty(required = true, value = "")
  @NotNull
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
   * Get payment reference
   *
   * @return payment reference
   */
  @ApiModelProperty(value = "")
  @Valid
  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public Application paymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
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

  public Application artifacts(List<Artifact> artifacts) {
    this.artifacts = artifacts;
    return this;
  }

  public Application addArtifactsItem(Artifact artifactsItem) {
    if (this.artifacts == null) {
      this.artifacts = new ArrayList<>();
    }
    this.artifacts.add(artifactsItem);
    return this;
  }

  /**
   * Get artifacts
   *
   * @return artifacts
   */
  @ApiModelProperty(value = "")
  @Valid
  public List<Artifact> getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(List<Artifact> artifacts) {
    this.artifacts = artifacts;
  }

  public ApplicationStatusField getApplicationStatus() {
    return applicationStatus;
  }

  public void setApplicationStatus(ApplicationStatusField applicationStatus) {
    this.applicationStatus = applicationStatus;
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
        && Objects.equals(this.paymentTaken, application.paymentTaken)
        && Objects.equals(this.submissionDate, application.submissionDate)
        && Objects.equals(this.existingBadgeNumber, application.existingBadgeNumber)
        && Objects.equals(this.party, application.party)
        && Objects.equals(this.eligibility, application.eligibility)
        && Objects.equals(this.artifacts, application.artifacts)
        && Objects.equals(this.applicationStatus, application.applicationStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        applicationId,
        applicationTypeCode,
        localAuthorityCode,
        paymentTaken,
        submissionDate,
        existingBadgeNumber,
        party,
        eligibility,
        artifacts,
        applicationStatus);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("applicationId", applicationId)
        .append("applicationTypeCode", applicationTypeCode)
        .append("localAuthorityCode", localAuthorityCode)
        .append("paymentTaken", paymentTaken)
        .append("submissionDate", submissionDate)
        .append("existingBadgeNumber", existingBadgeNumber)
        .append("party", party)
        .append("eligibility", eligibility)
        .append("paymentReference", paymentReference)
        .append("artifacts", artifacts)
        .append("applicationStatus", applicationStatus)
        .toString();
  }
}
