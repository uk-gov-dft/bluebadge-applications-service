package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import static java.time.Duration.ofMinutes;
import static java.time.Period.ofYears;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.pagehelper.Page;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationStatusField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.BreathlessnessTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.BulkyEquipmentTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TransferApplicationParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class ApplicationRepositoryIntTest extends ApplicationContextTests {

  @Autowired private ApplicationRepository applicationRepository;

  @Test
  public void create_all() {
    ApplicationEntity entity =
        ApplicationEntity.builder()
            .id(UUID.randomUUID())
            .appTypeCode("NEW")
            .contactBuildingStreet("Street")
            .contactEmailAddress("me@mydomian.com")
            .contactLine2("Line2")
            .contactName("Mr Contact")
            .contactPostcode("CN123TC")
            .contactTownCity("Town")
            .isPaymentTaken(true)
            .paymentReference("mypayref")
            .localAuthorityCode("ABERD")
            .partyCode("PERSON")
            .primaryPhoneNo("01234567890")
            .secondaryPhoneNo("01234567890")
            .submissionDatetime(OffsetDateTime.now(Clock.systemUTC()).toInstant())
            .armsAdaptedVehDesc("Car")
            .armsDrivingFreq("drive frq")
            .armsIsAdaptedVehicle(true)
            .benefitExpiryDate(LocalDate.now())
            .benefitIsIndefinite(false)
            .blindRegisteredAtLaCode("ABERD")
            .dob(LocalDate.now().minus(ofYears(30)))
            .eligibilityCode("PIP")
            .walkOtherDesc("walk desc")
            .breathlessnessOtherDesc("breathlessness desc")
            .orgIsCharity(true)
            .orgCharityNo("123456")
            .noOfBadges(1)
            .holderNameAtBirth("name at birth")
            .eligibilityConditions("elig conditions")
            .holderName("holderName")
            .applicationStatus("TODO")
            .build();
    assertEquals(1, applicationRepository.createApplication(entity));

    List<HealthcareProfessionalEntity> pros = new ArrayList<>();
    pros.add(
        HealthcareProfessionalEntity.builder()
            .applicationId(entity.getId())
            .profLocation("location")
            .profName("pro name")
            .build());
    assertEquals(1, applicationRepository.createHealthcareProfessionals(pros));

    List<MedicationEntity> meds = new ArrayList<>();
    meds.add(
        MedicationEntity.builder()
            .applicationId(entity.getId())
            .frequency("freq")
            .isPrescribed(true)
            .name("med name")
            .quantity("med quantity")
            .build());
    assertEquals(1, applicationRepository.createMedications(meds));

    List<TreatmentEntity> treats = new ArrayList<>();
    treats.add(
        TreatmentEntity.builder()
            .applicationId(entity.getId())
            .description("desc")
            .time("Hours")
            .build());
    assertEquals(1, applicationRepository.createTreatments(treats));

    List<VehicleEntity> vehicles = new ArrayList<>();
    vehicles.add(
        VehicleEntity.builder()
            .applicationId(entity.getId())
            .registrationNumber("ABC123")
            .usageFrequency("often")
            .build());
    assertEquals(1, applicationRepository.createVehicles(vehicles));

    List<WalkingAidEntity> aids = new ArrayList<>();
    aids.add(
        WalkingAidEntity.builder()
            .applicationId(entity.getId())
            .description("desc")
            .usage("usage")
            .build());
    assertEquals(1, applicationRepository.createWalkingAids(aids));

    List<WalkingDifficultyTypeEntity> diffs = new ArrayList<>();
    diffs.add(
        WalkingDifficultyTypeEntity.builder()
            .applicationId(entity.getId())
            .typeCode("PAIN")
            .build());
    assertEquals(1, applicationRepository.createWalkingDifficultyTypes(diffs));

    List<BreathlessnessTypeEntity> breathlessnessTypes = new ArrayList<>();
    breathlessnessTypes.add(
        BreathlessnessTypeEntity.builder()
            .applicationId(entity.getId())
            .typeCode("UPHILL")
            .build());
    breathlessnessTypes.add(
        BreathlessnessTypeEntity.builder().applicationId(entity.getId()).typeCode("OTHER").build());
    assertEquals(2, applicationRepository.createBreathlessnessTypes(breathlessnessTypes));

    List<BulkyEquipmentTypeEntity> eqs = new ArrayList<>();
    eqs.add(
        BulkyEquipmentTypeEntity.builder().applicationId(entity.getId()).typeCode("OTHER").build());
    assertEquals(1, applicationRepository.createBulkyEquipment(eqs));

    List<ArtifactEntity> artifactEntities = new ArrayList<>();
    artifactEntities.add(
        ArtifactEntity.builder()
            .applicationId(entity.getId())
            .type("PROOF_ID")
            .link("/some/link/to/an/artifact")
            .build());
    assertEquals(1, applicationRepository.createArtifacts(artifactEntities));
  }

  @Test
  public void find_moreThan50() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").build();
    Page<ApplicationSummaryEntity> results = applicationRepository.findApplications(params, 1, 50);

    assertThat(results.size()).isEqualTo(50);
    assertThat(results.getTotal()).isGreaterThan(50);
    assertThat(results.getPageSize()).isEqualTo(50);
    assertThat(results.getPageNum()).isEqualTo(1);
    assertThat(results.getPages()).isGreaterThan(1);
  }

  @Test
  public void find_moreThan50_2ndPage() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").build();
    Page<ApplicationSummaryEntity> results = applicationRepository.findApplications(params, 2, 50);

    assertThat(results.size()).isLessThan(50);
    assertThat(results.getTotal()).isGreaterThan(50);
    assertThat(results.getPageSize()).isEqualTo(50);
    assertThat(results.getPageNum()).isEqualTo(2);
    assertThat(results.getPages()).isGreaterThan(1);
  }

  @Test
  public void find_allForLa() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("XXXXXX").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(10, results.size());
  }

  @Test
  public void find_one() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").name("FindOneTest").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(1, results.size());
    ApplicationSummaryEntity entity = results.get(0);
    assertEquals(
        UUID.fromString("2166bd0b-7086-4a79-8c10-5dde52bcee73"), entity.getApplicationId());
    assertEquals("INPROGRESS", entity.getApplicationStatus());
    assertEquals("DLA", entity.getEligibilityCode());
    assertEquals("FindOneTest", entity.getHolderName());
    assertEquals("Nino2", entity.getNino());
    assertEquals("PERSON", entity.getPartyTypeCode());
    assertEquals("ZZ111ZZ", entity.getPostcode());
    assertEquals(Instant.parse("2011-01-01T03:00:00.00Z"), entity.getSubmissionDate());
    assertEquals(LocalDate.of(1970, 5, 29), entity.getDob());
  }

  @Test
  public void find_dateRange() {
    // Given data contains a single record with date 10 years ago
    Instant from = ZonedDateTime.now().minus(ofYears(10)).toInstant();
    from = from.minus(ofMinutes(1));
    Instant to = ZonedDateTime.now().minus(ofYears(10)).toInstant();
    to = to.plus(ofMinutes(1));
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").from(from).to(to).build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(1, results.size());
    assertEquals("10years back", results.get(0).getHolderName());
  }

  @Test
  public void find_appType() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder()
            .authorityCode("ABERD")
            .applicationTypeCode(ApplicationTypeCodeField.CANCEL.name())
            .build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      assertEquals("CANCEL", result.getApplicationTypeCode());
    }
  }

  @Test
  public void find_postcode() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").postcode("zz11 1ZZ").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      assertEquals("ZZ111ZZ", result.getPostcode());
    }
  }

  @Test
  public void find_name() {
    // Given a badge holder with name Holder Name

    // When retrieve by name as zz99 (case insensitive and wild card start/end)
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").name("olDER Na").build();

    // Then application returned
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      assertEquals("Holder Name", result.getHolderName());
    }
  }

  @Test
  public void retrieve() {
    UUID appUuid = UUID.fromString("1087ac26-491a-46f0-9006-36187dc40764");
    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder().uuid(appUuid).deleted(Boolean.FALSE).build();
    ApplicationEntity result = applicationRepository.retrieveApplication(params);
    // Healthcare Professionals
    assertEquals(2, result.getHealthcareProfessionals().size());
    assertTrue(
        result
            .getHealthcareProfessionals()
            .contains(
                HealthcareProfessionalEntity.builder()
                    .applicationId(result.getId())
                    .profName("Prof Name")
                    .profLocation("Prof Location")
                    .build()));
    assertTrue(
        result
            .getHealthcareProfessionals()
            .contains(
                HealthcareProfessionalEntity.builder()
                    .applicationId(result.getId())
                    .profName("Prof Name2")
                    .profLocation("Prof Location2")
                    .build()));
    // Medications
    assertEquals(2, result.getMedications().size());
    assertTrue(
        result
            .getMedications()
            .contains(
                MedicationEntity.builder()
                    .applicationId(result.getId())
                    .name("Med Name")
                    .frequency("Med Frequency")
                    .quantity("Med Quantity")
                    .isPrescribed(true)
                    .build()));
    assertTrue(
        result
            .getMedications()
            .contains(
                MedicationEntity.builder()
                    .applicationId(result.getId())
                    .name("Med Name2")
                    .frequency("Med Frequency2")
                    .quantity("Med Quantity2")
                    .isPrescribed(true)
                    .build()));
    // Treatments
    assertEquals(2, result.getTreatments().size());
    assertTrue(
        result
            .getTreatments()
            .contains(
                TreatmentEntity.builder()
                    .applicationId(result.getId())
                    .description("Description")
                    .time("Time")
                    .build()));
    assertTrue(
        result
            .getTreatments()
            .contains(
                TreatmentEntity.builder()
                    .applicationId(result.getId())
                    .description("Description2")
                    .time("Time2")
                    .build()));
    // Vehicles
    assertEquals(2, result.getVehicles().size());
    assertTrue(
        result
            .getVehicles()
            .contains(
                VehicleEntity.builder()
                    .applicationId(result.getId())
                    .registrationNumber("ER1")
                    .typeCode("CAR")
                    .usageFrequency("Usage Frequency")
                    .build()));
    assertTrue(
        result
            .getVehicles()
            .contains(
                VehicleEntity.builder()
                    .applicationId(result.getId())
                    .registrationNumber("ER2")
                    .typeCode("CAR")
                    .usageFrequency("Usage Frequency2")
                    .build()));
    // Walking Aids
    assertEquals(2, result.getWalkingAids().size());
    assertTrue(
        result
            .getWalkingAids()
            .contains(
                WalkingAidEntity.builder()
                    .applicationId(result.getId())
                    .howProvidedCode("PRIVATE")
                    .description("Aid Description")
                    .usage("Aid Usage")
                    .build()));
    assertTrue(
        result
            .getWalkingAids()
            .contains(
                WalkingAidEntity.builder()
                    .applicationId(result.getId())
                    .howProvidedCode("PRIVATE")
                    .description("Aid Description2")
                    .usage("Aid Usage2")
                    .build()));

    // Walking Difficulty Types
    assertEquals(2, result.getWalkingDifficultyTypes().size());
    assertTrue(
        result
            .getWalkingDifficultyTypes()
            .contains(
                WalkingDifficultyTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("PAIN")
                    .build()));
    assertTrue(
        result
            .getWalkingDifficultyTypes()
            .contains(
                WalkingDifficultyTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("BREATH")
                    .build()));
    // Breathlessness Types
    assertEquals(2, result.getBreathlessnessTypes().size());
    assertTrue(
        result
            .getBreathlessnessTypes()
            .contains(
                BreathlessnessTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("UPHILL")
                    .build()));
    assertTrue(
        result
            .getBreathlessnessTypes()
            .contains(
                BreathlessnessTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("OTHER")
                    .build()));

    // Bulky Equipment Types
    assertEquals(2, result.getBulkyEquipment().size());
    assertTrue(
        result
            .getBulkyEquipment()
            .contains(
                BulkyEquipmentTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("SUCTION")
                    .build()));
    assertTrue(
        result
            .getBulkyEquipment()
            .contains(
                BulkyEquipmentTypeEntity.builder()
                    .applicationId(result.getId())
                    .typeCode("OTHER")
                    .build()));

    // Application
    assertEquals("ABERD", result.getLocalAuthorityCode());
    assertEquals("REPLACE", result.getAppTypeCode());
    assertTrue(result.getIsPaymentTaken());
    assertEquals("mypayref", result.getPaymentReference());
    Instant expectedSubmission = Instant.parse("2011-01-01T03:00:00Z");
    assertEquals(expectedSubmission, result.getSubmissionDatetime());
    assertEquals("AAAAAA", result.getExistingBadgeNo());
    assertEquals("PERSON", result.getPartyCode());
    assertEquals("Contact Name", result.getContactName());
    assertEquals("Contact Building Street", result.getContactBuildingStreet());
    assertEquals("Contact Line2", result.getContactLine2());
    assertEquals("Contact Town City", result.getContactTownCity());
    assertEquals("ZZ111ZZ", result.getContactPostcode());
    assertEquals("PPN", result.getPrimaryPhoneNo());
    assertEquals("SPN", result.getSecondaryPhoneNo());
    assertEquals("Contact Email Address", result.getContactEmailAddress());
    assertEquals("Holder Name", result.getHolderName());
    assertTrue(result.getOrgIsCharity());
    assertEquals("Org Charity No", result.getOrgCharityNo());
    assertEquals(new Integer(1), result.getNoOfBadges());
    assertEquals("Nino", result.getNino());
    assertEquals(LocalDate.parse("1970-05-29"), result.getDob());
    assertEquals("MALE", result.getGenderCode());
    assertEquals("Holder Name At Birth", result.getHolderNameAtBirth());
    assertEquals("DLA", result.getEligibilityCode());
    assertEquals("Eligibility Conditions", result.getEligibilityConditions());
    assertTrue(result.getBenefitIsIndefinite());
    assertEquals(LocalDate.parse("2020-01-31"), result.getBenefitExpiryDate());
    assertEquals("Walk Other Desc", result.getWalkOtherDesc());
    assertEquals("Breathlessness Other Desc", result.getBreathlessnessOtherDesc());
    assertEquals("LESSMIN", result.getWalkLengthCode());
    assertEquals("SLOW", result.getWalkSpeedCode());
    assertEquals("Arms Driving Freq", result.getArmsDrivingFreq());
    assertTrue(result.getArmsIsAdaptedVehicle());
    assertEquals("Arms Adapted Veh Desc", result.getArmsAdaptedVehDesc());
    assertEquals("BIRM", result.getBlindRegisteredAtLaCode());
    assertEquals("Bulky1", result.getBulkyEquipmentOtherDesc());
    assertEquals("ABERD", result.getTransferredLaFromCode());
    assertEquals(Instant.parse("2010-12-31T03:15:00Z"), result.getTransferredFromLaDatetime());
    assertThat(result.getArtifacts()).hasSize(1);
    assertThat(result.getArtifacts())
        .extracting("applicationId", "type", "link")
        .containsOnly(tuple(appUuid, "PROOF_ID", "link/to/artifact1"));
  }

  @Test
  public void delete() {

    UUID application_id = UUID.fromString("0bd06c01-a193-4255-be0b-0fbee253ee5e");

    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder()
            .uuid(application_id)
            .deleted(Boolean.FALSE)
            .build();

    ApplicationEntity result = applicationRepository.retrieveApplication(params);
    assertEquals(2, result.getHealthcareProfessionals().size());
    assertEquals(2, result.getMedications().size());
    assertEquals(2, result.getTreatments().size());
    assertEquals(2, result.getVehicles().size());
    assertEquals(2, result.getWalkingAids().size());
    assertEquals(2, result.getWalkingDifficultyTypes().size());
    assertEquals(2, result.getBreathlessnessTypes().size());
    assertEquals(2, result.getBulkyEquipment().size());
    assertEquals(1, result.getArtifacts().size());

    assertEquals("LIVER", result.getLocalAuthorityCode());
    assertFalse(result.getIsDeleted());
    assertNull(result.getDeletedTimestamp());

    applicationRepository.deleteApplication(params);
    applicationRepository.deleteBulkyEquipmentTypes(application_id.toString());

    params =
        RetrieveApplicationQueryParams.builder()
            .uuid(UUID.fromString("0bd06c01-a193-4255-be0b-0fbee253ee5e"))
            .deleted(Boolean.TRUE)
            .build();
    ApplicationEntity deleted = applicationRepository.retrieveApplication(params);

    assertNull(deleted.getExistingBadgeNo());
    assertNull(deleted.getContactName());
    assertNull(deleted.getContactLine2());
    assertNull(deleted.getContactEmailAddress());
    assertNull(deleted.getOrgIsCharity());
    assertNull(deleted.getOrgCharityNo());
    assertNull(deleted.getNoOfBadges());
    assertNull(deleted.getNino());
    assertNull(deleted.getDob());
    assertNull(deleted.getHolderNameAtBirth());
    assertNull(deleted.getEligibilityConditions());
    assertNull(deleted.getBenefitIsIndefinite());
    assertNull(deleted.getBenefitExpiryDate());
    assertNull(deleted.getWalkOtherDesc());
    assertNull(deleted.getBreathlessnessOtherDesc());
    assertNull(deleted.getWalkLengthCode());
    assertNull(deleted.getArmsDrivingFreq());
    assertNull(deleted.getArmsIsAdaptedVehicle());
    assertNull(deleted.getArmsAdaptedVehDesc());
    assertNull(deleted.getBlindRegisteredAtLaCode());
    assertEquals(0, deleted.getBulkyEquipment().size());
    assertNull(deleted.getSecondaryPhoneNo());

    assertEquals("DELETED", deleted.getContactBuildingStreet());
    assertEquals("DELETED", deleted.getContactTownCity());
    assertEquals("DELETED", deleted.getContactPostcode());
    assertEquals("DELETED", deleted.getPrimaryPhoneNo());
    assertEquals("DELETED", deleted.getHolderName());

    assertTrue(deleted.getIsDeleted());
    assertNotNull(deleted.getDeletedTimestamp());
  }

  @Test
  public void deleteArtifacts() {
    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder()
            .uuid(UUID.fromString("0bd06c01-a193-4255-be0b-0fbee253ee5e"))
            .deleted(Boolean.FALSE)
            .build();

    ApplicationEntity result = applicationRepository.retrieveApplication(params);
    assertThat(result.getArtifacts()).hasSize(1);

    String appId = "0bd06c01-a193-4255-be0b-0fbee253ee5e";
    int deletedCount = applicationRepository.deleteArtifacts(appId);
    assertThat(deletedCount).isEqualTo(1);

    result = applicationRepository.retrieveApplication(params);
    assertThat(result.getArtifacts()).isEmpty();
  }

  @Test
  public void transfer() {
    TransferApplicationParams transferParams =
        TransferApplicationParams.builder()
            .transferFromLaShortCode("ABERD")
            .transferToLaShortCode("SHROP")
            .applicationId(UUID.fromString("2166bd0b-7086-4a79-8c10-5dde52bcee68"))
            .build();
    RetrieveApplicationQueryParams retrieveParams =
        RetrieveApplicationQueryParams.builder()
            .uuid(UUID.fromString("2166bd0b-7086-4a79-8c10-5dde52bcee68"))
            .deleted(Boolean.FALSE)
            .build();

    ApplicationEntity result = applicationRepository.retrieveApplication(retrieveParams);
    assertThat(result.getApplicationStatus()).isEqualTo(ApplicationStatusField.INPROGRESS.name());
    assertThat(result.getLocalAuthorityCode()).isEqualTo("ABERD");

    applicationRepository.transferApplication(transferParams);
    result = applicationRepository.retrieveApplication(retrieveParams);
    assertThat(result.getApplicationStatus()).isEqualTo(ApplicationStatusField.TODO.name());
    assertThat(result.getLocalAuthorityCode()).isEqualTo("SHROP");
    assertThat(result.getTransferredLaFromCode()).isEqualTo("ABERD");
    // Making a bit of an assumption that DB time is somewhere near accurate.
    assertThat(result.getTransferredFromLaDatetime())
        .isCloseTo(Instant.now(), within(10, ChronoUnit.MINUTES));
  }
}
