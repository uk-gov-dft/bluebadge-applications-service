package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Builder
@Data
@Alias("ApplicationSummaryEntity")
public class ApplicationSummaryEntity {
  UUID applicationId;
  String partyTypeCode;
  String applicationTypeCode;
  String nino;
  String holderName;
  Instant submissionDate;
  String eligibilityCode;
  String postcode;
  String applicationStatus;
}
