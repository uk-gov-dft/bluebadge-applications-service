package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Benefit;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class BenefitConverter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresBenefit(entity.getEligibilityCode())) {

      ensureHasEligibility(model);
      if (null == model.getEligibility().getBenefit()) {
        model.getEligibility().setBenefit(new Benefit());
      }
      Benefit benefit = model.getEligibility().getBenefit();
      benefit.setExpiryDate(entity.getBenefitExpiryDate());
      benefit.setIsIndefinite(entity.getBenefitIsIndefinite());
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility()) {
      return;
    }
    if (null != model.getEligibility().getBenefit()) {
      entity.setBenefitExpiryDate(model.getEligibility().getBenefit().getExpiryDate());
      entity.setBenefitIsIndefinite(model.getEligibility().getBenefit().isIsIndefinite());
    }
  }
}
