package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Vehicle;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.VehicleTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class VehicleConverterTest {

  private static final String REGISTRATION_NUMBER = "VK61VXX";
  private static final String USAGE_FREQUENCY = "USAGE";
  private static final VehicleTypeCodeField VEHICLE_TYPE_CODE_FIELD = VehicleTypeCodeField.CAR;
  private final VehicleConverter converter;

  public VehicleConverterTest(VehicleConverter converter) {
    this.converter = converter;
  }


  @Test
  public void mapToEntity() {
    Vehicle vehicle = new Vehicle();
    vehicle.setRegistrationNumber(REGISTRATION_NUMBER);
    vehicle.setTypeCode(VEHICLE_TYPE_CODE_FIELD);
    vehicle.setUsageFrequency(USAGE_FREQUENCY);
    UUID uuid = UUID.randomUUID();

    VehicleEntity entity = converter.mapToEntity(vehicle, uuid);

    assertEquals(vehicle.getRegistrationNumber(), entity.getRegistrationNumber());
    assertEquals(vehicle.getTypeCode().toString(), entity.getTypeCode());
    assertEquals(vehicle.getUsageFrequency(), entity.getUsageFrequency());
    assertEquals(uuid, entity.getApplicationId());
  }

  @Test
  public void mapToModel() {
    VehicleEntity entity = VehicleEntity.builder()
        .usageFrequency(USAGE_FREQUENCY)
        .registrationNumber(REGISTRATION_NUMBER)
        .typeCode(VEHICLE_TYPE_CODE_FIELD.name()).build();

    Vehicle model = converter.mapToModel(entity);

    assertEquals(model.getRegistrationNumber(), REGISTRATION_NUMBER);
    assertEquals(model.getUsageFrequency(), USAGE_FREQUENCY);
    assertEquals(model.getTypeCode(), VEHICLE_TYPE_CODE_FIELD);
  }
}
