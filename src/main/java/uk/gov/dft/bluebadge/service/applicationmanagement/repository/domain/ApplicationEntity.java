package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/** Bean to hold a ApplicationEntity record. */
@Alias("ApplicationEntity")
@Data
@Builder
public class ApplicationEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;
  private String localAuthorityCode;
  private String transferredLaFromCode;
  private Instant transferredFromLaDatetime;
  private String appTypeCode;
  private Boolean isPaymentTaken;
  private String paymentReference;
  private Instant submissionDatetime;
  private String existingBadgeNo;
  private String partyCode;
  private String contactName;
  private String contactBuildingStreet;
  private String contactLine2;
  private String contactTownCity;
  private String contactPostcode;
  private String primaryPhoneNo;
  private String secondaryPhoneNo;
  private String contactEmailAddress;
  private String holderName;
  private Boolean orgIsCharity;
  private String orgCharityNo;
  private Integer noOfBadges;
  private String nino;
  private LocalDate dob;
  private String genderCode;
  private String holderNameAtBirth;
  private String eligibilityCode;
  private String eligibilityConditions;
  private Boolean benefitIsIndefinite;
  private LocalDate benefitExpiryDate;
  private String walkPainDesc;
  private String walkBalanceDesc;
  private Boolean walkBalanceHealthProdForFall;
  private String walkDangerDesc;
  private Boolean walkDangerChestLungHeartEpilepsy;
  private String walkOtherDesc;
  private String walkLengthCode;
  private String walkSpeedCode;
  private String armsDrivingFreq;
  private Boolean armsIsAdaptedVehicle;
  private String armsAdaptedVehDesc;
  private String blindRegisteredAtLaCode;
  private String bulkyEquipmentOtherDesc;
  private List<HealthcareProfessionalEntity> healthcareProfessionals;
  private List<VehicleEntity> vehicles;
  private List<WalkingDifficultyTypeEntity> walkingDifficultyTypes;
  private List<BreathlessnessTypeEntity> breathlessnessTypes;
  private List<WalkingAidEntity> walkingAids;
  private List<TreatmentEntity> treatments;
  private List<MedicationEntity> medications;
  private List<BulkyEquipmentTypeEntity> bulkyEquipment;
  private List<ArtifactEntity> artifacts;
  private Boolean isDeleted;
  private LocalDate deletedTimestamp;
  private String applicationStatus;
  private String breathlessnessOtherDesc;
}
