package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Vehicle;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;

public class VehicleConverterTest extends ApplicationFixture {

  private final VehicleConverter converter = new VehicleConverter();

  @Test
  public void mapToEntity() {
    Vehicle vehicle = new Vehicle();
    vehicle.setRegistrationNumber(ValidValues.VEH_REG);
    vehicle.setTypeCode(ValidValues.VEH_TYPE);
    vehicle.setUsageFrequency(ValidValues.VEH_USAGE);

    VehicleEntity entity = converter.mapToEntity(vehicle, UUID.fromString(ValidValues.ID));

    assertEquals(ValidValues.VEH_REG, entity.getRegistrationNumber());
    assertEquals(ValidValues.VEH_TYPE.name(), entity.getTypeCode());
    assertEquals(ValidValues.VEH_USAGE, entity.getUsageFrequency());
    assertEquals(UUID.fromString(ValidValues.ID), entity.getApplicationId());
  }

  @Test
  public void mapToModel() {
    VehicleEntity entity = getFullyPopulatedApplicationEntity().getVehicles().get(0);

    Vehicle model = converter.mapToModel(entity);

    assertEquals(ValidValues.VEH_REG, model.getRegistrationNumber());
    assertEquals(ValidValues.VEH_USAGE, model.getUsageFrequency());
    assertEquals(ValidValues.VEH_TYPE, model.getTypeCode());
  }
}
