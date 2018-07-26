package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Treatment;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;

public class TreatmentConverterTest {

  @Test
  public void mapToEntity() {
    Treatment model = new Treatment();
    model.setDescription("ABC");
    model.setTime("TIME");
    UUID uuid = UUID.randomUUID();

    TreatmentConverter converter = new TreatmentConverter();
    TreatmentEntity entity = converter.mapToEntity(model, uuid);

    assertEquals(uuid, entity.getApplicationId());
    assertEquals(model.getDescription(), entity.getDescription());
    assertEquals(model.getTime(), entity.getTime());
  }
}
