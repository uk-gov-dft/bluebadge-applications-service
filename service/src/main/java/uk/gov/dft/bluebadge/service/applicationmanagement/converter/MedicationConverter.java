package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Medication;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;

public class MedicationConverter
    extends ApplicationToEntityCollection<MedicationEntity, Medication> {
  @Override
  MedicationEntity mapToEntity(Medication model, UUID applicationId) {
    return MedicationEntity.builder()
        .applicationId(applicationId)
        .frequency(model.getFrequency())
        .isPrescribed(model.isIsPrescribed())
        .name(model.getName())
        .quantity(model.getQuantity())
        .build();
  }
}
