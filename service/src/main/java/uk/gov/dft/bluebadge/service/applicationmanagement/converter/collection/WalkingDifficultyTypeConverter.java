package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

@Component
public class WalkingDifficultyTypeConverter
    implements ApplicationToEntityCollection<
        WalkingDifficultyTypeEntity, WalkingDifficultyTypeCodeField> {
  @Override
  public WalkingDifficultyTypeEntity mapToEntity(
      WalkingDifficultyTypeCodeField model, UUID applicationId) {
    return WalkingDifficultyTypeEntity.builder()
        .applicationId(applicationId)
        .typeCode(model.toString())
        .build();
  }
}
