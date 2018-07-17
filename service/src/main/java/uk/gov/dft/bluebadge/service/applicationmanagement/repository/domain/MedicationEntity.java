package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicationEntity {

  private UUID applicationId;
  private String name;
  private Boolean isPrescribed;
  private String frequency;
  private String quantity;
}
