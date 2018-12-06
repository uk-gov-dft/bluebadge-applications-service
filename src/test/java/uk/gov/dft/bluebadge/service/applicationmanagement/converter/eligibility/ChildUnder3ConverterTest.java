package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.BulkyEquipmentTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class ChildUnder3ConverterTest extends ApplicationFixture {

  private final ChildUnder3Converter converter =
      new ChildUnder3Converter(new BulkyEquipmentTypeConverter());

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.CHILDBULK.name());
    Application model = new Application();
    converter.convertToModel(model, entity);

    assertEquals(
        ValidValues.BULKY_MEDICAL_EQUIPMENT_TYPE_CODE_FIELD,
        model.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode());
  }

  @Test
  public void convertToEntity() {
    Application model =
        getApplicationBuilder().addBaseApplication().setEligibilityChildBulk().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    converter.convertToEntity(model, entity);

    assertEquals(
        ValidValues.BULKY_MEDICAL_EQUIPMENT_TYPE_CODE_FIELD.name(),
        entity.getBulkyEquipment().get(0).getTypeCode());
  }
}
