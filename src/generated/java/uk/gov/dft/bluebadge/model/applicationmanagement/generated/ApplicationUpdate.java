package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Application */
@Validated
public class ApplicationUpdate {

  // Supplied in the original PUT request
  @JsonProperty("applicationStatus")
  private ApplicationStatusField applicationStatus = null;

  // Added for use in the repository
  @JsonIgnore
  private UUID applicationId;

  /**
   * The unique number for this application - a UUID
   *
   * @return applicationId
   */
  @ApiModelProperty(
    example = "12345678-1234-1234-1234-123456781234",
    value = "The unique number for this application - a UUID"
  )
  public UUID getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(UUID applicationId) {
    this.applicationId = applicationId;
  }


  /**
   * Get applicationStatus
   *
   * @return applicationStatus
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public ApplicationStatusField getApplicationStatusField() {
    return applicationStatus;
  }

  public void setApplicationStatus(ApplicationStatusField applicationStatus) {
    this.applicationStatus = applicationStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationUpdate application = (ApplicationUpdate) o;
    return Objects.equals(this.applicationId, application.applicationId)
        && Objects.equals(this.applicationStatus, application.applicationStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        applicationId,
        applicationStatus);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("applicationStatus", applicationStatus)
      .append("applicationId", applicationId)
      .toString();
  }



}
