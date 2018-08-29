package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.DisabilityArms;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.HealthcareProfessionalConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.MedicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.TreatmentConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingAidConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingDifficultyTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
class EligibilityConverter implements ApplicationBiConverter {

  private final WalkingDifficultyTypeConverter walkingDifficultyTypeConverter;
  private final WalkingAidConverter walkingAidConverter;
  private final TreatmentConverter treatmentConverter;
  private final MedicationConverter medicationConverter;
  private final HealthcareProfessionalConverter healthcareProfessionalConverter;

  @Autowired
  EligibilityConverter(
      WalkingDifficultyTypeConverter walkingDifficultyTypeConverter,
      WalkingAidConverter walkingAidConverter,
      TreatmentConverter treatmentConverter,
      MedicationConverter medicationConverter,
      HealthcareProfessionalConverter healthcareProfessionalConverter) {
    this.walkingDifficultyTypeConverter = walkingDifficultyTypeConverter;
    this.walkingAidConverter = walkingAidConverter;
    this.treatmentConverter = treatmentConverter;
    this.medicationConverter = medicationConverter;
    this.healthcareProfessionalConverter = healthcareProfessionalConverter;
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    // TODO
  }

  public void convertToEntity(Application application, ApplicationEntity entity) {
    Eligibility eligibility = application.getEligibility();

    // Can be null if org application
    if (null != eligibility) {
      convertRootObjects(eligibility, entity);
      convertBenefit(eligibility, entity);
      convertWalkingDifficulty(eligibility, entity);
      convertDisabilityArms(eligibility, entity);
      convertBlind(eligibility, entity);
      convertChildUnder3(eligibility, entity);
    }
  }

  void convertRootObjects(Eligibility eligibility, ApplicationEntity entity) {
    Assert.notNull(
        eligibility.getTypeCode(),
        "Eligibility type code must not be null.  Bean validation should have stopped this.");
    entity.setEligibilityCode(eligibility.getTypeCode().toString());
    entity.setEligibilityConditions(eligibility.getDescriptionOfConditions());
    entity.setHealthcareProfessionals(
        healthcareProfessionalConverter.convertToEntityList(
            eligibility.getHealthcareProfessionals(), entity.getId()));
  }

  void convertChildUnder3(Eligibility eligibility, ApplicationEntity entity) {
    if (null != eligibility.getChildUnder3()) {
      entity.setBulkyEquipmentTypeCode(
          eligibility.getChildUnder3().getBulkyMedicalEquipmentTypeCode().toString());
    }
  }

  void convertBlind(Eligibility eligibility, ApplicationEntity entity) {
    if (null != eligibility.getBlind()) {
      entity.setBlindRegisteredAtLaCode(eligibility.getBlind().getRegisteredAtLaId());
    }
  }

  void convertDisabilityArms(Eligibility eligibility, ApplicationEntity entity) {
    if (null != eligibility.getDisabilityArms()) {
      DisabilityArms disabilityArms = eligibility.getDisabilityArms();
      entity.setArmsAdaptedVehDesc(disabilityArms.getAdaptedVehicleDescription());
      entity.setArmsDrivingFreq(disabilityArms.getDrivingFrequency());
      entity.setArmsIsAdaptedVehicle(disabilityArms.isIsAdaptedVehicle());
    }
  }

  void convertWalkingDifficulty(Eligibility eligibility, ApplicationEntity entity) {
    if (null != eligibility.getWalkingDifficulty()) {
      WalkingDifficulty walkingDifficulty = eligibility.getWalkingDifficulty();

      // Root fields
      Assert.notNull(
          walkingDifficulty.getWalkingLengthOfTimeCode(),
          "Walking length of time should not be null.  Bean is not null");
      entity.setWalkLengthCode(walkingDifficulty.getWalkingLengthOfTimeCode().toString());
      if (null != walkingDifficulty.getWalkingSpeedCode()) {
        entity.setWalkSpeedCode(walkingDifficulty.getWalkingSpeedCode().toString());
      }
      entity.setWalkOtherDesc(walkingDifficulty.getOtherDescription());

      // Lists
      entity.setWalkingDifficultyTypes(
          walkingDifficultyTypeConverter.convertToEntityList(
              walkingDifficulty.getTypeCodes(), entity.getId()));
      entity.setWalkingAids(
          walkingAidConverter.convertToEntityList(
              walkingDifficulty.getWalkingAids(), entity.getId()));
      entity.setTreatments(
          treatmentConverter.convertToEntityList(
              walkingDifficulty.getTreatments(), entity.getId()));
      entity.setMedications(
          medicationConverter.convertToEntityList(
              walkingDifficulty.getMedications(), entity.getId()));
    }
  }

  void convertBenefit(Eligibility eligibility, ApplicationEntity entity) {
    if (null != eligibility.getBenefit()) {
      entity.setBenefitIsIndefinite(eligibility.getBenefit().isIsIndefinite());
      entity.setBenefitExpiryDate(eligibility.getBenefit().getExpiryDate());
    }
  }
}
