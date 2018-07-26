package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

public class HealthcareProfessionalConverterTest {

  @Test
  public void mapToEntity() {
    HealthcareProfessional model = new HealthcareProfessional();
    model.setLocation("LOC");
    model.setName("NA");
    UUID uuid = UUID.randomUUID();

    HealthcareProfessionalConverter converter = new HealthcareProfessionalConverter();
    HealthcareProfessionalEntity entity = converter.mapToEntity(model, uuid);

    assertEquals(uuid, entity.getApplicationId());
    assertEquals(model.getLocation(), entity.getProfLocation());
    assertEquals(model.getName(), entity.getProfName());
  }
}
