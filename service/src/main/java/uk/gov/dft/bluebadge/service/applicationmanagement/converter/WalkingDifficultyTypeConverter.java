package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

@Component
class WalkingDifficultyTypeConverter
    extends ApplicationToEntityCollection<
        WalkingDifficultyTypeEntity, WalkingDifficultyTypeCodeField> {
  @Override
  WalkingDifficultyTypeEntity mapToEntity(
      WalkingDifficultyTypeCodeField model, UUID applicationId) {
    return WalkingDifficultyTypeEntity.builder()
        .applicationId(applicationId)
        .typeCode(model.toString())
        .build();
  }
}
