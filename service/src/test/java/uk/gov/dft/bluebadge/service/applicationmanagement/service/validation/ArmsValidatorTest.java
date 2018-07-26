package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ArmsValidatorTest extends ApplicationFixture{

  @Test
  public void validateArms() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    ArmsValidator armsValidator = new ArmsValidator();
    resetErrors(app);

    // Valid first
    armsValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Cant have adapted vehicle text if not adapted
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(false);
    app.getEligibility().getDisabilityArms().setAdaptedVehicleDescription("ddd");
    resetErrors(app);
    armsValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION));
  }
}