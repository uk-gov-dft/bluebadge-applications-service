package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HowProvidedCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingAid;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;

public class WalkingAidConverterTest extends ApplicationFixture {

  WalkingAidConverter converter = new WalkingAidConverter();

  @Test
  public void mapToEntity() {
    WalkingAid model = new WalkingAid();
    model.setDescription(ValidValues.WALK_AID_DESC);
    model.setHowProvidedCode(HowProvidedCodeField.fromValue(ValidValues.WALK_AID_PROVIDED));
    model.setUsage(ValidValues.WALK_AID_USAGE);

    WalkingAidEntity entity = converter.mapToEntity(model, UUID.fromString(ValidValues.ID));
    assertEquals(ValidValues.WALK_AID_DESC, entity.getDescription());
    assertEquals(ValidValues.WALK_AID_PROVIDED, entity.getHowProvidedCode());
    assertEquals(UUID.fromString(ValidValues.ID), entity.getApplicationId());
    assertEquals(ValidValues.WALK_AID_USAGE, entity.getUsage());
  }

  @Test
  public void mapToModel() {
    WalkingAidEntity entity = getFullyPopulatedApplicationEntity().getWalkingAids().get(0);
    WalkingAid model = converter.mapToModel(entity);

    assertEquals(ValidValues.WALK_AID_USAGE, model.getUsage());
    assertEquals(ValidValues.WALK_AID_DESC, model.getDescription());
    assertEquals(ValidValues.WALK_AID_PROVIDED, model.getHowProvidedCode().name());
  }
}
