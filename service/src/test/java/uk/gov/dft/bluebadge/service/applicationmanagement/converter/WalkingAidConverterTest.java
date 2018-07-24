package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HowProvidedCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingAid;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;

public class WalkingAidConverterTest {

  @Test
  public void convertToEntity() {
    UUID uuid = UUID.randomUUID();
    WalkingAid model = new WalkingAid();
    model.setDescription("ABC");
    model.setHowProvidedCode(HowProvidedCodeField.PRESCRIBE);
    model.setUsage("USAGE");
    WalkingAidConverter converter = new WalkingAidConverter();

    WalkingAidEntity entity = converter.mapToEntity(model, uuid);
    assertEquals("ABC", entity.getDescription());
    assertEquals("PRESCRIBE", entity.getHowProvidedCode());
    assertEquals(uuid, entity.getApplicationId());
    assertEquals("USAGE", entity.getUsage());
  }
}
