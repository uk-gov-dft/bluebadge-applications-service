package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ChildUnder3;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class ChildUnder3Converter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresChildUnder3Object(entity.getEligibilityCode())) {
      ensureHasEligibility(model);

      if (null == model.getEligibility().getChildUnder3()) {
        model.getEligibility().setChildUnder3(new ChildUnder3());
      }

      ChildUnder3 childUnder3 = model.getEligibility().getChildUnder3();
      childUnder3.setBulkyMedicalEquipmentTypeCode(
          BulkyMedicalEquipmentTypeCodeField.fromValue(entity.getBulkyEquipmentTypeCode()));
      if (BulkyMedicalEquipmentTypeCodeField.OTHER
          == childUnder3.getBulkyMedicalEquipmentTypeCode()) {
        childUnder3.setOtherMedicalEquipment(entity.getBulkyEquipmentOtherDesc());
      }
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility() || null == model.getEligibility().getChildUnder3()) {
      return;
    }

    entity.setBulkyEquipmentTypeCode(
        model.getEligibility().getChildUnder3().getBulkyMedicalEquipmentTypeCode().name());
    if (BulkyMedicalEquipmentTypeCodeField.OTHER
        == BulkyMedicalEquipmentTypeCodeField.fromValue(entity.getBulkyEquipmentTypeCode())) {
      entity.setBulkyEquipmentOtherDesc(
          model.getEligibility().getChildUnder3().getOtherMedicalEquipment());
    }
  }
}
