package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Blind;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class BlindConverter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresBlind(entity.getEligibilityCode())) {
      ensureHasEligibility(model);
      if (null == model.getEligibility().getBlind()) {
        model.getEligibility().setBlind(new Blind());
      }
      model.getEligibility().getBlind().setRegisteredAtLaId(entity.getBlindRegisteredAtLaCode());
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility()) {
      return;
    }
    if (null != model.getEligibility().getBlind()) {
      entity.setBlindRegisteredAtLaCode(model.getEligibility().getBlind().getRegisteredAtLaId());
    }
  }
}
