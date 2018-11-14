package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ChildUnder3;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.BulkyEquipmentTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import java.util.UUID;

@Component
public class ChildUnder3Converter implements ApplicationBiConverter {

  private BulkyEquipmentTypeConverter bulkyEquipmentTypeConverter;

  @Autowired
  ChildUnder3Converter(BulkyEquipmentTypeConverter bulkyEquipmentTypeConverter) {
    this.bulkyEquipmentTypeConverter = bulkyEquipmentTypeConverter;
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresChildUnder3Object(entity.getEligibilityCode())) {
      ensureHasEligibility(model);

      if (null == model.getEligibility().getChildUnder3()) {
        model.getEligibility().setChildUnder3(new ChildUnder3());
      }

      ChildUnder3 childUnder3 = model.getEligibility().getChildUnder3();
      // BBB-1033 next property to be deprecated in API.
      // Validation ensures there is at least 1 bulky equipment value
      // Populate with 1st value
      childUnder3.setBulkyMedicalEquipmentTypeCode(
          BulkyMedicalEquipmentTypeCodeField.fromValue(
              entity.getBulkyEquipment().get(0).getTypeCode()));
      // End of deprecation

      childUnder3.setBulkyMedicalEquipmentTypeCodes(
          bulkyEquipmentTypeConverter.convertToModelList(entity.getBulkyEquipment()));
      childUnder3.setOtherMedicalEquipment(entity.getBulkyEquipmentOtherDesc());
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility() || null == model.getEligibility().getChildUnder3()) {
      return;
    }

    // BBB-1033 the non list version of bulky equipment is to be deprecated.
    // Only present in request from citizen app if here, so should not be present.
    // When fully deprecated first branch of if needs removing.
    if (null != model.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode()) {
      entity.setBulkyEquipment(
          bulkyEquipmentTypeConverter.convertToEntityList(
              Lists.newArrayList(
                  model.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode()),
              UUID.fromString(model.getApplicationId())));
    } else {
      entity.setBulkyEquipment(
          bulkyEquipmentTypeConverter.convertToEntityList(
              model.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCodes(),
              UUID.fromString(model.getApplicationId())));
    }
    entity.setBulkyEquipmentOtherDesc(
        model.getEligibility().getChildUnder3().getOtherMedicalEquipment());
  }
}
