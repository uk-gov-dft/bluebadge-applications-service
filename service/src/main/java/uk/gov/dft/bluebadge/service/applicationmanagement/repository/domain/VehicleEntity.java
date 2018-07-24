package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID applicationId;
  private String registrationNumber;
  private String typeCode;
  private String usageFrequency;
}
