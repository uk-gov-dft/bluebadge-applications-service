package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import static org.junit.Assert.assertEquals;

public class BlindConverterTest extends ApplicationFixture {

  private final BlindConverter converter = new BlindConverter();

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.BLIND.name());
    Application model = new Application();
    converter.convertToModel(model, entity);

    assertEquals(ValidValues.LA_CODE, model.getEligibility().getBlind().getRegisteredAtLaId());
  }

  @Test
  public void convertToEntity() {
    Application model = getApplicationBuilder().addBaseApplication().setEligibilityBlind().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    converter.convertToEntity(model, entity);

    assertEquals(ValidValues.LA_CODE, entity.getBlindRegisteredAtLaCode());
  }
}
