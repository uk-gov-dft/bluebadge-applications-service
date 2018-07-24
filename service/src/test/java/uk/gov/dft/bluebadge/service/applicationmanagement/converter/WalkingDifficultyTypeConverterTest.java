package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

public class WalkingDifficultyTypeConverterTest {

  @Test
  public void mapToEntity() {
    WalkingDifficultyTypeCodeField model = WalkingDifficultyTypeCodeField.SOMELSE;
    UUID uuid = UUID.randomUUID();
    WalkingDifficultyTypeConverter converter = new WalkingDifficultyTypeConverter();
    WalkingDifficultyTypeEntity entity = converter.mapToEntity(model, uuid);
    assertEquals(uuid, entity.getApplicationId());
    assertEquals("SOMELSE", entity.getTypeCode());
  }
}
