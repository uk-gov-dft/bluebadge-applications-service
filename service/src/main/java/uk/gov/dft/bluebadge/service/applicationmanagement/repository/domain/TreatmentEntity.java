package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TreatmentEntity {
  private UUID applicationId;
  private String description;
  private String time;
}
