package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ArmsValidatorTest extends ApplicationFixture {

  private ArmsValidator armsValidator = new ArmsValidator();

  @Test
  public void validateArms_new() {

    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // Valid first
    armsValidator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Cant have adapted vehicle text if not adapted
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(false);
    app.getEligibility().getDisabilityArms().setAdaptedVehicleDescription("ddd");
    errors = getNewBindingResult(app);

    armsValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION));
  }

  @Test
  public void validateArms__nullArmsObject() {
    // Given an app with no arms data
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    app.getEligibility().setDisabilityArms(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // When validate
    armsValidator.validate(app, errors);
    // No exceptions
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void validateArms__nullArmsMandatoryFields_newApplication() {
    // Given an app with no arms data
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(null);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // When validate
    armsValidator.validate(app, errors);
    // Binding error
    assertEquals(2, errors.getErrorCount());
    // Error for null Boolean
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS_VEH_IS_ADAPTED));
    // Error for not null description, when the Boolean above is not True
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_ARMS_VEH_ADAPTION));
  }

  @Test
  public void validateArms__nullArmsMandatoryFields_renewApplication() {
    // Given an app with no arms data
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();
    app.getEligibility().getDisabilityArms().setIsAdaptedVehicle(null);
    app.getEligibility().getDisabilityArms().setAdaptedVehicleDescription(null);
    app.setApplicationTypeCode(ApplicationTypeCodeField.RENEW);
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // When validate
    armsValidator.validate(app, errors);
    // No exceptions
    assertEquals(0, errors.getErrorCount());
  }
}
