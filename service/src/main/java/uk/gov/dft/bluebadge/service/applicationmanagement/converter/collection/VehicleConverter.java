package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Vehicle;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;

@Component
public class VehicleConverter implements ApplicationToEntityCollection<VehicleEntity, Vehicle> {

  @Override
  public VehicleEntity mapToEntity(Vehicle vehicle, UUID applicationId) {
    return VehicleEntity.builder()
        .registrationNumber(vehicle.getRegistrationNumber())
        .typeCode(vehicle.getTypeCode().toString())
        .usageFrequency(vehicle.getUsageFrequency())
        .applicationId(applicationId)
        .build();
  }
}
