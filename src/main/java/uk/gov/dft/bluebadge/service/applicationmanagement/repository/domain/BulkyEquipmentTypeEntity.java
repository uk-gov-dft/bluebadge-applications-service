package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@Alias("WalkingDifficultyTypeEntity")
public class BulkyEquipmentTypeEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private UUID applicationId;
  private String typeCode;
}
