package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import static java.time.Duration.ofMinutes;
import static java.time.Period.ofYears;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
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
            .orgIsCharity(true)
            .orgCharityNo("123456")
            .noOfBadges(1)
            .holderNameAtBirth("name at birth")
            .eligibilityConditions("elig conditions")
            .holderName("holderName")
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
  }

  @Test
  public void find_moreThan50() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(50, results.size());
  }

  @Test
  public void find_allForLa() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("XXXXXX").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(10, results.size());
  }

  @Test
  public void find_dateRange() {
    // Given data contains a single record with date 10 years ago
    Instant from = ZonedDateTime.now().minus(ofYears(10)).toInstant();
    from = from.minus(ofMinutes(1));
    Instant to = ZonedDateTime.now().minus(ofYears(10)).toInstant();
    to = to.plus(ofMinutes(1));
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder()
            .authorityCode("ABERD")
            .submissionFrom(from)
            .submissionTo(to)
            .build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    assertEquals(1, results.size());
    assertEquals("10years back", results.get(0).getHolderName());
  }

  @Test
  public void find_appType() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder()
            .authorityCode("ABERD")
            .applicationTypeCode(ApplicationTypeCodeField.CANCEL)
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
    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder()
            .uuid(UUID.fromString("1087ac26-491a-46f0-9006-36187dc40764"))
            .authorityCode("ABERD")
            .build();
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
    // Application
    assertEquals("ABERD", result.getLocalAuthorityCode());
    assertEquals("REPLACE", result.getAppTypeCode());
    assertTrue(result.getIsPaymentTaken());
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
    assertEquals("LESSMIN", result.getWalkLengthCode());
    assertEquals("SLOW", result.getWalkSpeedCode());
    assertEquals("Arms Driving Freq", result.getArmsDrivingFreq());
    assertTrue(result.getArmsIsAdaptedVehicle());
    assertEquals("Arms Adapted Veh Desc", result.getArmsAdaptedVehDesc());
    assertEquals("BIRM", result.getBlindRegisteredAtLaCode());
    assertEquals("SUCTION", result.getBulkyEquipmentTypeCode());
    assertEquals("Url Proof Eligibility", result.getUrlProofEligibility());
    assertEquals("Url Proof Address", result.getUrlProofAddress());
    assertEquals("Url Proof Identity", result.getUrlProofIdentity());
    assertEquals("Url Badge Photo", result.getUrlBadgePhoto());
  }

  @Test
  public void retrieve_wrongAuthority() {
    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder()
            .uuid(UUID.fromString("1087ac26-491a-46f0-9006-36187dc40764"))
            .authorityCode("BIRM")
            .build();
    ApplicationEntity result = applicationRepository.retrieveApplication(params);
    assertNull(result);
  }
}
