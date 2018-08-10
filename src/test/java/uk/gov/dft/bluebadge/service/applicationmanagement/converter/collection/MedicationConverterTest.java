package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Medication;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;

public class MedicationConverterTest {

  @Test
  public void mapToEntity() {
    Medication model = new Medication();
    model.setFrequency("ABC");
    model.setIsPrescribed(true);
    model.setName("name");
    model.setQuantity("quantity");
    UUID uuid = UUID.randomUUID();

    MedicationConverter converter = new MedicationConverter();

    MedicationEntity entity = converter.mapToEntity(model, uuid);

    assertEquals(uuid, entity.getApplicationId());
    assertEquals(model.getFrequency(), entity.getFrequency());
    assertEquals(model.getName(), entity.getName());
    assertEquals(model.getQuantity(), entity.getQuantity());
    assertEquals(model.isIsPrescribed(), entity.getIsPrescribed());
  }
}
