package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

public class HealthcareProfessionalConverterTest extends ApplicationFixture {

  private HealthcareProfessionalConverter converter = new HealthcareProfessionalConverter();

  @Test
  public void mapToEntity() {
    HealthcareProfessional model = new HealthcareProfessional();
    model.setLocation(ValidValues.PROFESSIONAL_LOCATION);
    model.setName(ValidValues.PROFESSIONAL_NAME);

    HealthcareProfessionalEntity entity =
        converter.mapToEntity(model, UUID.fromString(ValidValues.ID));

    assertEquals(ValidValues.ID, entity.getApplicationId().toString());
    assertEquals(ValidValues.PROFESSIONAL_LOCATION, entity.getProfLocation());
    assertEquals(ValidValues.PROFESSIONAL_NAME, entity.getProfName());
  }

  @Test
  public void mapToModel() {
    HealthcareProfessionalEntity entity =
        getFullyPopulatedApplicationEntity().getHealthcareProfessionals().get(0);
    HealthcareProfessional model = converter.mapToModel(entity);

    assertEquals(ValidValues.PROFESSIONAL_NAME, model.getName());
    assertEquals(ValidValues.PROFESSIONAL_LOCATION, model.getLocation());
  }
}
