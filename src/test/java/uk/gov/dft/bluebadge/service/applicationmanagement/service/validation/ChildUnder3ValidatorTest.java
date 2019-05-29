package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ChildUnder3;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ChildUnder3ValidatorTest extends ApplicationFixture {

  private ChildUnder3Validator validator = new ChildUnder3Validator();

  @Test
  public void validateOtherDescription() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityChildBulk().build();
    ChildUnder3 childUnder3 = application.getEligibility().getChildUnder3();

    // Other description must be present if type code other.
    childUnder3.setBulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.OTHER);
    childUnder3.setOtherMedicalEquipment("");
    BeanPropertyBindingResult errors = getNewBindingResult(application);

    validator.validate(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3_OTHER_DESC));

    // And Other description must not be present for other types
    childUnder3.setBulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.OXYADMIN);
    childUnder3.setOtherMedicalEquipment("Stuff");
    errors = getNewBindingResult(application);

    validator.validate(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_CHILD3_OTHER_DESC));

    // And passes validation otherwise....
    childUnder3.setBulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.OTHER);
    childUnder3.setOtherMedicalEquipment("Stuff");
    errors = getNewBindingResult(application);

    validator.validate(application, errors);
    assertEquals(0, errors.getErrorCount());

    childUnder3.setBulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.OXYADMIN);
    childUnder3.setOtherMedicalEquipment(null);
    errors = getNewBindingResult(application);

    validator.validate(application, errors);
    assertEquals(0, errors.getErrorCount());
  }
}
