package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Medication;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;

@Component
public class MedicationConverter
    implements ApplicationToEntityCollection<MedicationEntity, Medication> {
  @Override
  public MedicationEntity mapToEntity(Medication model, UUID applicationId) {
    return MedicationEntity.builder()
        .applicationId(applicationId)
        .frequency(model.getFrequency())
        .isPrescribed(model.isIsPrescribed())
        .name(model.getName())
        .quantity(model.getQuantity())
        .build();
  }

  @Override
  public Medication mapToModel(MedicationEntity entity) {
    // TODO
    return null;
  }
}
