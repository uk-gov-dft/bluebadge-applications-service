package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ArmsValidatorTest extends ApplicationFixture {

  @Test
  public void validateArms() {
    ArmsValidator armsValidator = new ArmsValidator();
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build());

    // Valid first
    armsValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Cant have adapted vehicle text if not adapted
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(false);
    app.getEligibility().getDisabilityArms().setAdaptedVehicleDescription("ddd");
    reset();
    armsValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION));
  }
}
