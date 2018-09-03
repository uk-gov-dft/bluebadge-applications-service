package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.DisabilityArms;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class DisabilityArmsConverterTest extends ApplicationFixture {

  private final DisabilityArmsConverter converter = new DisabilityArmsConverter();

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.ARMS.name());
    Application model = new Application();
    converter.convertToModel(model, entity);

    DisabilityArms arms = model.getEligibility().getDisabilityArms();
    assertEquals(ValidValues.ARMS_ADAPTED_DESC, arms.getAdaptedVehicleDescription());
    assertEquals(ValidValues.ARMS_DRIVE_FREQ, arms.getDrivingFrequency());
    assertEquals(ValidValues.ARMS_IS_ADAPTED, arms.isIsAdaptedVehicle());
  }

  @Test
  public void convertToEntity() {
    Application model = getApplicationBuilder().addBaseApplication().setEligibilityArms().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    converter.convertToEntity(model, entity);

    assertEquals(ValidValues.ARMS_IS_ADAPTED, entity.getArmsIsAdaptedVehicle());
    assertEquals(ValidValues.ARMS_DRIVE_FREQ, entity.getArmsDrivingFreq());
    assertEquals(ValidValues.ARMS_ADAPTED_DESC, entity.getArmsAdaptedVehDesc());
  }
}
