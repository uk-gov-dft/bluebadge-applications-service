package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.DisabilityArms;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Person;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class ApplicationConverter implements ToEntityConverter<ApplicationEntity, Application> {

  private final VehicleConverter vehicleConverter;
  private final WalkingDifficultyTypeConverter walkingDifficultyTypeConverter;
  private final WalkingAidConverter walkingAidConverter;
  private final TreatmentConverter treatmentConverter;
  private final MedicationConverter medicationConverter;
  private final HealthcareProfessionalConverter healthcareProfessionalConverter;

  public ApplicationConverter(
      VehicleConverter vehicleConverter,
      WalkingDifficultyTypeConverter walkingDifficultyTypeConverter,
      WalkingAidConverter walkingAidConverter,
      TreatmentConverter treatmentConverter,
      MedicationConverter medicationConverter,
      HealthcareProfessionalConverter healthcareProfessionalConverter) {
    this.vehicleConverter = vehicleConverter;
    this.walkingDifficultyTypeConverter = walkingDifficultyTypeConverter;
    this.walkingAidConverter = walkingAidConverter;
    this.treatmentConverter = treatmentConverter;
    this.medicationConverter = medicationConverter;
    this.healthcareProfessionalConverter = healthcareProfessionalConverter;
  }

  private void convertPerson(Application application, ApplicationEntity entity) {
    Person person = application.getParty().getPerson();
    entity.setHolderName(person.getBadgeHolderName());
    entity.setNino(person.getNino());
    entity.setDob(person.getDob());
    entity.setHolderNameAtBirth(person.getNameAtBirth());
    entity.setGenderCode(person.getGenderCode().toString());
    entity.setNoOfBadges(1);

    Eligibility eligibility = application.getEligibility();
    if (null != eligibility) {
      entity.setEligibilityCode(eligibility.getTypeCode().toString());
      entity.setEligibilityConditions(eligibility.getDescriptionOfConditions());
      if (null != eligibility.getBenefit()) {
        entity.setBenefitIsIndefinite(eligibility.getBenefit().isIsIndefinite());
        entity.setBenefitExpiryDate(eligibility.getBenefit().getExpiryDate());
      }
      if (null != eligibility.getWalkingDifficulty()) {
        WalkingDifficulty walkingDifficulty = eligibility.getWalkingDifficulty();
        entity.setWalkingDifficultyTypes(
            walkingDifficultyTypeConverter.convertToEntityList(
                walkingDifficulty.getTypeCodes(), entity.getId()));
        entity.setWalkOtherDesc(walkingDifficulty.getOtherDescription());
        entity.setWalkingAids(
            walkingAidConverter.convertToEntityList(
                walkingDifficulty.getWalkingAids(), entity.getId()));
        entity.setWalkLengthCode(walkingDifficulty.getWalkingLengthOfTimeCode().toString());
        entity.setWalkSpeedCode(walkingDifficulty.getWalkingSpeedCode().toString());
        entity.setTreatments(
            treatmentConverter.convertToEntityList(
                walkingDifficulty.getTreatments(), entity.getId()));
        entity.setMedications(
            medicationConverter.convertToEntityList(
                walkingDifficulty.getMedications(), entity.getId()));
      }
      if (null != eligibility.getDisabilityArms()) {
        DisabilityArms disabilityArms = eligibility.getDisabilityArms();
        entity.setArmsAdaptedVehDesc(disabilityArms.getAdaptedVehicleDescription());
        entity.setArmsDrivingFreq(disabilityArms.getDrivingFrequency());
        entity.setArmsIsAdaptedVehicle(disabilityArms.isIsAdaptedVehicle());
      }
      entity.setHealthcareProfessionals(
          healthcareProfessionalConverter.convertToEntityList(
              eligibility.getHealthcareProfessionals(), entity.getId()));
      if (null != eligibility.getBlind()) {
        entity.setBlindRegisteredAtLaCode(eligibility.getBlind().getRegisteredAtLaId());
      }
      if (null != eligibility.getChildUnder3()) {
        entity.setBulkyEquipmentTypeCode(
            eligibility.getChildUnder3().getBulkyMedicalEquipmentTypeCode().toString());
      }
    }
  }

  @Override
  public ApplicationEntity convertToEntity(Application application) {

    Contact contact = application.getParty().getContact();

    // Populate general fields
    ApplicationEntity entity =
        ApplicationEntity.builder()
            .appTypeCode(application.getApplicationTypeCode().toString())
            .id(UUID.fromString(application.getApplicationId()))
            .localAuthorityCode(application.getLocalAuthorityCode())
            .isPaymentTaken(application.isIsPaymentTaken())
            .submissionDatetime(application.getSubmissionDate())
            .existingBadgeNo(application.getExistingBadgeNumber())
            .contactName(contact.getFullName())
            .contactBuildingStreet(contact.getBuildingStreet())
            .contactLine2(contact.getLine2())
            .contactTownCity(contact.getTownCity())
            .contactEmailAddress(contact.getEmailAddress())
            .contactPostcode(ConvertUtils.formatPostcodeForEntity(contact.getPostCode()))
            .primaryPhoneNo(contact.getPrimaryPhoneNumber())
            .secondaryPhoneNo(contact.getSecondaryPhoneNumber())
            .partyCode(application.getParty().getTypeCode().toString())
            .build();

    if (application.getParty().getTypeCode().equals(PartyTypeCodeField.PERSON)) {
      convertPerson(application, entity);
    } else {
      convertOrg(application, entity);
    }

    return entity;
  }

  private void convertOrg(Application application, ApplicationEntity entity) {
    Organisation organisation = application.getParty().getOrganisation();
    entity.setHolderName(organisation.getBadgeHolderName());
    entity.setOrgIsCharity(organisation.isIsCharity());
    entity.setOrgCharityNo(organisation.getCharityNumber());
    entity.setNoOfBadges(organisation.getNumberOfBadges());
    entity.setVehicles(
        vehicleConverter.convertToEntityList(organisation.getVehicles(), entity.getId()));
  }

  public ApplicationEntity convertToEntityOnCreate(Application application) {
    if (null == application.getApplicationId()) {
      application.setApplicationId(UUID.randomUUID().toString());
    }
    return convertToEntity(application);
  }
}
