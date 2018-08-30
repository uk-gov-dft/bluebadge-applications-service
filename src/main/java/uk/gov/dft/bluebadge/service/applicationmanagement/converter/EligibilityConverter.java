package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.HealthcareProfessionalConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.BenefitConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.BlindConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.ChildUnder3Converter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.DisabilityArmsConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.WalkingDifficultyConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
class EligibilityConverter implements ApplicationBiConverter {

  private final List<ApplicationBiConverter> converters = new ArrayList<>();
  private final HealthcareProfessionalConverter healthcareProfessionalConverter;

  @Autowired
  EligibilityConverter(
      ChildUnder3Converter childUnder3Converter,
      BlindConverter blindConverter,
      DisabilityArmsConverter disabilityArmsConverter,
      WalkingDifficultyConverter walkingDifficultyConverter,
      BenefitConverter benefitConverter,
      HealthcareProfessionalConverter healthcareProfessionalConverter) {
    this.healthcareProfessionalConverter = healthcareProfessionalConverter;
    converters.add(childUnder3Converter);
    converters.add(blindConverter);
    converters.add(disabilityArmsConverter);
    converters.add(walkingDifficultyConverter);
    converters.add(benefitConverter);
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)) {

      ensureHasEligibility(model);
      Eligibility eligibility = model.getEligibility();
      eligibility.setTypeCode(EligibilityCodeField.fromValue(entity.getEligibilityCode()));
      eligibility.setDescriptionOfConditions(entity.getEligibilityConditions());
      eligibility.setHealthcareProfessionals(
          healthcareProfessionalConverter.convertToModelList(entity.getHealthcareProfessionals()));

      for (ApplicationBiConverter converter : converters) {
        converter.convertToModel(model, entity);
      }
    }
  }

  public void convertToEntity(Application model, ApplicationEntity entity) {
    Eligibility eligibility = model.getEligibility();

    // Can be null if org application
    if (null != eligibility) {
      Assert.notNull(
          eligibility.getTypeCode(),
          "Eligibility type code must not be null.  Bean validation should have stopped this.");
      entity.setEligibilityCode(eligibility.getTypeCode().toString());
      entity.setEligibilityConditions(eligibility.getDescriptionOfConditions());
      entity.setHealthcareProfessionals(
          healthcareProfessionalConverter.convertToEntityList(
              eligibility.getHealthcareProfessionals(), UUID.fromString(model.getApplicationId())));
    }

    for (ApplicationBiConverter converter : converters) {
      converter.convertToEntity(model, entity);
    }
  }
}
