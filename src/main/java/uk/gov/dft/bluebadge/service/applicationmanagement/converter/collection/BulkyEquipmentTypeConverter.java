package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.BulkyEquipmentTypeEntity;

@Component
public class BulkyEquipmentTypeConverter
    implements ApplicationToEntityCollection<
        BulkyEquipmentTypeEntity, BulkyMedicalEquipmentTypeCodeField> {

  @Override
  public BulkyEquipmentTypeEntity mapToEntity(
      BulkyMedicalEquipmentTypeCodeField model, UUID applicationId) {
    return BulkyEquipmentTypeEntity.builder()
        .applicationId(applicationId)
        .typeCode(model.toString())
        .build();
  }

  @Override
  public BulkyMedicalEquipmentTypeCodeField mapToModel(BulkyEquipmentTypeEntity entity) {
    return BulkyMedicalEquipmentTypeCodeField.fromValue(entity.getTypeCode());
  }
}
