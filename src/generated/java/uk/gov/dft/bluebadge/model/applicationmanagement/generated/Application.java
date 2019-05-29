package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode
public class Application {

  private String applicationId;
  @NotNull private ApplicationTypeCodeField applicationTypeCode;
  @NotNull private String localAuthorityCode;
  private String transferredLaFromCode;
  private OffsetDateTime transferredFromLaDate;
  @NotNull private Boolean paymentTaken;
  private OffsetDateTime submissionDate;
  private String existingBadgeNumber;
  @Valid @NotNull private Party party;

  @Valid private Eligibility eligibility;

  private String paymentReference;
  @Valid private List<Artifact> artifacts;
  private ApplicationStatusField applicationStatus;

  public boolean isRenewal() {
    return ApplicationTypeCodeField.RENEW == applicationTypeCode;
  }
}
