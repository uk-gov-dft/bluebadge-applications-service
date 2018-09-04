package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Medication;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;

public class MedicationConverterTest extends ApplicationFixture {

  private MedicationConverter converter = new MedicationConverter();

  @Test
  public void mapToEntity() {
    Medication model = new Medication();
    model.setFrequency(ValidValues.MED_FREQ);
    model.setIsPrescribed(ValidValues.MED_IS_PRESCRIBED);
    model.setName(ValidValues.MED_NAME);
    model.setQuantity(ValidValues.MED_QUANTITY);

    MedicationEntity entity = converter.mapToEntity(model, UUID.fromString(ValidValues.ID));

    assertEquals(UUID.fromString(ValidValues.ID), entity.getApplicationId());
    assertEquals(ValidValues.MED_FREQ, entity.getFrequency());
    assertEquals(ValidValues.MED_NAME, entity.getName());
    assertEquals(ValidValues.MED_QUANTITY, entity.getQuantity());
    assertEquals(ValidValues.MED_IS_PRESCRIBED, entity.getIsPrescribed());
  }

  @Test
  public void mapToModel() {
    MedicationEntity entity = getFullyPopulatedApplicationEntity().getMedications().get(0);

    Medication model = converter.mapToModel(entity);

    assertEquals(ValidValues.MED_IS_PRESCRIBED, model.isIsPrescribed());
    assertEquals(ValidValues.MED_QUANTITY, model.getQuantity());
    assertEquals(ValidValues.MED_NAME, model.getName());
    assertEquals(ValidValues.MED_FREQ, model.getFrequency());
  }
}
