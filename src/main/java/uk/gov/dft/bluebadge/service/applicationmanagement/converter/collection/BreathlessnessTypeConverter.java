package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BreathlessnessTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.BreathlessnessTypeEntity;

@Component
public class BreathlessnessTypeConverter
    implements ApplicationToEntityCollection<
        BreathlessnessTypeEntity, BreathlessnessTypeCodeField> {

  @Override
  public BreathlessnessTypeEntity mapToEntity(
      BreathlessnessTypeCodeField model, UUID applicationId) {
    return BreathlessnessTypeEntity.builder()
        .applicationId(applicationId)
        .typeCode(model.toString())
        .build();
  }

  @Override
  public BreathlessnessTypeCodeField mapToModel(BreathlessnessTypeEntity entity) {
    return BreathlessnessTypeCodeField.valueOf(entity.getTypeCode());
  }
}
