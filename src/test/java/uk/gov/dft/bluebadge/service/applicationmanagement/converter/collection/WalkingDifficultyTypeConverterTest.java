package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

public class WalkingDifficultyTypeConverterTest extends ApplicationFixture {

  private WalkingDifficultyTypeConverter converter = new WalkingDifficultyTypeConverter();

  @Test
  public void mapToEntity() {
    WalkingDifficultyTypeCodeField model = ValidValues.WALKING_DIFFICULTY_TYPE_CODE_FIELD;

    WalkingDifficultyTypeEntity entity =
        converter.mapToEntity(model, UUID.fromString(ValidValues.ID));
    assertEquals(UUID.fromString(ValidValues.ID), entity.getApplicationId());
    assertEquals(ValidValues.WALKING_DIFFICULTY_TYPE_CODE_FIELD.name(), entity.getTypeCode());
  }

  @Test
  public void mapToModel() {
    WalkingDifficultyTypeEntity entity =
        getFullyPopulatedApplicationEntity().getWalkingDifficultyTypes().get(0);

    WalkingDifficultyTypeCodeField model = converter.mapToModel(entity);

    assertEquals(ValidValues.WALKING_DIFFICULTY_TYPE_CODE_FIELD, model);
  }
}
