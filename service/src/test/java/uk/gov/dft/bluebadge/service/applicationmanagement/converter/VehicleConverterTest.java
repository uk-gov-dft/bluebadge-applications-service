package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Vehicle;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.VehicleTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;

public class VehicleConverterTest {

  @Test
  public void mapToEntity() {
    Vehicle vehicle = new Vehicle();
    vehicle.setRegistrationNumber("VK61VXX");
    vehicle.setTypeCode(VehicleTypeCodeField.CAR);
    vehicle.setUsageFrequency("USAGE");
    UUID uuid = UUID.randomUUID();

    VehicleConverter converter = new VehicleConverter();
    VehicleEntity entity = converter.mapToEntity(vehicle, uuid);

    assertEquals(vehicle.getRegistrationNumber(), entity.getRegistrationNumber());
    assertEquals(vehicle.getTypeCode().toString(), entity.getTypeCode());
    assertEquals(vehicle.getUsageFrequency(), entity.getUsageFrequency());
  }
}
