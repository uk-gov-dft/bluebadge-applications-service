package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Treatment;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;

public class TreatmentConverterTest extends ApplicationFixture {

  private TreatmentConverter converter = new TreatmentConverter();

  @Test
  public void mapToEntity() {
    Treatment model = new Treatment();
    model.setDescription(ValidValues.TREAT_DESC);
    model.setTime(ValidValues.TREAT_TIME);

    TreatmentEntity entity = converter.mapToEntity(model, UUID.fromString(ValidValues.ID));

    assertEquals(UUID.fromString(ValidValues.ID), entity.getApplicationId());
    assertEquals(model.getDescription(), entity.getDescription());
    assertEquals(model.getTime(), entity.getTime());
  }

  @Test
  public void mapToModel() {
    TreatmentEntity entity = getFullyPopulatedApplicationEntity().getTreatments().get(0);

    Treatment model = converter.mapToModel(entity);

    assertEquals(model.getDescription(), ValidValues.TREAT_DESC);
    assertEquals(model.getTime(), ValidValues.TREAT_TIME);
  }
}
