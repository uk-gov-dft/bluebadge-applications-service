package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Breathlessness;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingSpeedCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationBiConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.BreathlessnessTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.MedicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.TreatmentConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingAidConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingDifficultyTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class WalkingDifficultyConverter implements ApplicationBiConverter {

  private final WalkingDifficultyTypeConverter walkingDifficultyTypeConverter;
  private final WalkingAidConverter walkingAidConverter;
  private final TreatmentConverter treatmentConverter;
  private final MedicationConverter medicationConverter;
  private final BreathlessnessTypeConverter breathlessnessTypeConverter;

  @Autowired
  WalkingDifficultyConverter(
      WalkingDifficultyTypeConverter walkingDifficultyTypeConverter,
      WalkingAidConverter walkingAidConverter,
      TreatmentConverter treatmentConverter,
      MedicationConverter medicationConverter,
      BreathlessnessTypeConverter breathlessnessTypeConverter) {
    this.walkingDifficultyTypeConverter = walkingDifficultyTypeConverter;
    this.walkingAidConverter = walkingAidConverter;
    this.treatmentConverter = treatmentConverter;
    this.medicationConverter = medicationConverter;
    this.breathlessnessTypeConverter = breathlessnessTypeConverter;
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (isPersonApplication(entity)
        && EligibilityRules.requiresWalkingDifficulty(entity.getEligibilityCode())) {
      ensureHasEligibility(model);
      if (null == model.getEligibility().getWalkingDifficulty()) {
        model.getEligibility().setWalkingDifficulty(new WalkingDifficulty());
      }
      WalkingDifficulty walkingDifficulty = model.getEligibility().getWalkingDifficulty();

      walkingDifficulty.setWalkingLengthOfTimeCode(
          WalkingLengthOfTimeCodeField.fromValue(entity.getWalkLengthCode()));
      walkingDifficulty.setWalkingSpeedCode(
          WalkingSpeedCodeField.fromValue(entity.getWalkSpeedCode()));
      walkingDifficulty.setOtherDescription(entity.getWalkOtherDesc());

      // Lists
      walkingDifficulty.setTypeCodes(
          walkingDifficultyTypeConverter.convertToModelList(entity.getWalkingDifficultyTypes()));
      walkingDifficulty.setWalkingAids(
          walkingAidConverter.convertToModelList(entity.getWalkingAids()));
      walkingDifficulty.setTreatments(
          treatmentConverter.convertToModelList(entity.getTreatments()));
      walkingDifficulty.setMedications(
          medicationConverter.convertToModelList(entity.getMedications()));
      walkingDifficulty.setBreathlessness(
          new Breathlessness()
              .otherDescription(entity.getBreathlessnessOtherDesc())
              .typeCodes(
                  breathlessnessTypeConverter.convertToModelList(entity.getBreathlessnessTypes())));
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    if (null == model.getEligibility()) {
      return;
    }
    if (null != model.getEligibility().getWalkingDifficulty()) {
      WalkingDifficulty walkingDifficulty = model.getEligibility().getWalkingDifficulty();

      entity.setWalkLengthCode(walkingDifficulty.getWalkingLengthOfTimeCode().name());

      if (null != walkingDifficulty.getWalkingSpeedCode()) {
        entity.setWalkSpeedCode(walkingDifficulty.getWalkingSpeedCode().name());
      }
      entity.setWalkOtherDesc(walkingDifficulty.getOtherDescription());

      // Lists
      entity.setWalkingDifficultyTypes(
          walkingDifficultyTypeConverter.convertToEntityList(
              walkingDifficulty.getTypeCodes(), UUID.fromString(model.getApplicationId())));
      entity.setWalkingAids(
          walkingAidConverter.convertToEntityList(
              walkingDifficulty.getWalkingAids(), UUID.fromString(model.getApplicationId())));
      entity.setTreatments(
          treatmentConverter.convertToEntityList(
              walkingDifficulty.getTreatments(), UUID.fromString(model.getApplicationId())));
      entity.setMedications(
          medicationConverter.convertToEntityList(
              walkingDifficulty.getMedications(), UUID.fromString(model.getApplicationId())));
      if (null != walkingDifficulty.getBreathlessness()) {
        entity.setBreathlessnessOtherDesc(
            walkingDifficulty.getBreathlessness().getOtherDescription());
        entity.setBreathlessnessTypes(
            breathlessnessTypeConverter.convertToEntityList(
                walkingDifficulty.getBreathlessness().getTypeCodes(),
                UUID.fromString(model.getApplicationId())));
      }
    }
  }
}
