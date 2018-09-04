package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.DisabilityArms;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class DisabilityArmsConverter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresDisabilityArms(entity.getEligibilityCode())) {
      ensureHasEligibility(model);
      if (null == model.getEligibility().getDisabilityArms()) {
        model.getEligibility().setDisabilityArms(new DisabilityArms());
      }
      DisabilityArms arms = model.getEligibility().getDisabilityArms();
      arms.setAdaptedVehicleDescription(entity.getArmsAdaptedVehDesc());
      arms.setIsAdaptedVehicle(entity.getArmsIsAdaptedVehicle());
      arms.setDrivingFrequency(entity.getArmsDrivingFreq());
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility()) {
      return;
    }
    if (null != model.getEligibility().getDisabilityArms()) {
      DisabilityArms disabilityArms = model.getEligibility().getDisabilityArms();
      entity.setArmsAdaptedVehDesc(disabilityArms.getAdaptedVehicleDescription());
      entity.setArmsIsAdaptedVehicle(disabilityArms.isIsAdaptedVehicle());
      entity.setArmsDrivingFreq(disabilityArms.getDrivingFrequency());
    }
  }
}
