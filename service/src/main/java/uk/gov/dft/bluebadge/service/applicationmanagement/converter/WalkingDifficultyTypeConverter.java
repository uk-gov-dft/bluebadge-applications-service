package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

public class WalkingDifficultyTypeConverter
    extends ApplicationToEntityCollection<WalkingDifficultyTypeEntity, String> {
  @Override
  WalkingDifficultyTypeEntity mapToEntity(String model, UUID applicationId) {
    return WalkingDifficultyTypeEntity.builder()
        .applicationId(applicationId)
        .typeCode(model)
        .build();
  }
}
