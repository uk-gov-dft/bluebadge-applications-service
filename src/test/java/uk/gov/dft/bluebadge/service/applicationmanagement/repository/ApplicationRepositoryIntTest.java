package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import org.junit.Assert;
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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.time.Duration.ofMinutes;
import static java.time.Period.ofYears;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class ApplicationRepositoryIntTest extends ApplicationContextTests {

  @Autowired
  private ApplicationRepository applicationRepository;

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
    Assert.assertEquals(1, applicationRepository.createApplication(entity));

    List<HealthcareProfessionalEntity> pros = new ArrayList<>();
    pros.add(
        HealthcareProfessionalEntity.builder()
            .applicationId(entity.getId())
            .profLocation("location")
            .profName("pro name")
            .build());
    Assert.assertEquals(1, applicationRepository.createHealthcareProfessionals(pros));

    List<MedicationEntity> meds = new ArrayList<>();
    meds.add(
        MedicationEntity.builder()
            .applicationId(entity.getId())
            .frequency("freq")
            .isPrescribed(true)
            .name("med name")
            .quantity("med quantity")
            .build());
    Assert.assertEquals(1, applicationRepository.createMedications(meds));

    List<TreatmentEntity> treats = new ArrayList<>();
    treats.add(
        TreatmentEntity.builder()
            .applicationId(entity.getId())
            .description("desc")
            .time("Hours")
            .build());
    Assert.assertEquals(1, applicationRepository.createTreatments(treats));

    List<VehicleEntity> vehicles = new ArrayList<>();
    vehicles.add(
        VehicleEntity.builder()
            .applicationId(entity.getId())
            .registrationNumber("ABC123")
            .usageFrequency("often")
            .build());
    Assert.assertEquals(1, applicationRepository.createVehicles(vehicles));

    List<WalkingAidEntity> aids = new ArrayList<>();
    aids.add(
        WalkingAidEntity.builder()
            .applicationId(entity.getId())
            .description("desc")
            .usage("usage")
            .build());
    Assert.assertEquals(1, applicationRepository.createWalkingAids(aids));

    List<WalkingDifficultyTypeEntity> diffs = new ArrayList<>();
    diffs.add(
        WalkingDifficultyTypeEntity.builder()
            .applicationId(entity.getId())
            .typeCode("PAIN")
            .build());
    Assert.assertEquals(1, applicationRepository.createWalkingDifficultyTypes(diffs));
  }

  @Test
  public void find_moreThan50() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    Assert.assertEquals(50, results.size());
  }

  @Test
  public void find_allForLa() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("XXXXXX").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    Assert.assertEquals(10, results.size());
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

    Assert.assertEquals(1, results.size());
    Assert.assertEquals("10years back", results.get(0).getHolderName());
  }

  @Test
  public void find_appType() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder()
            .authorityCode("ABERD")
            .applicationTypeCode(ApplicationTypeCodeField.CANCEL)
            .build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    Assert.assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      Assert.assertEquals("CANCEL", result.getApplicationTypeCode());
    }
  }

  @Test
  public void find_postcode() {
    FindApplicationQueryParams params =
        FindApplicationQueryParams.builder().authorityCode("ABERD").postcode("zz11 1ZZ").build();
    List<ApplicationSummaryEntity> results = applicationRepository.findApplications(params);

    Assert.assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      Assert.assertEquals("ZZ111ZZ", result.getPostcode());
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

    Assert.assertTrue(!results.isEmpty());
    for (ApplicationSummaryEntity result : results) {
      Assert.assertEquals("Holder Name", result.getHolderName());
    }
  }

  @Test
  public void retrieve() {
    RetrieveApplicationQueryParams params = RetrieveApplicationQueryParams.builder()
        .uuid(UUID.fromString("1087ac26-491a-46f0-9006-36187dc40764")).build();
    ApplicationEntity result =
        applicationRepository.retrieveApplication(params);
    // Healthcare Professionals
    Assert.assertEquals("Prof Name", result.getHealthcareProfessionals().get(0).getProfName());
    Assert.assertEquals("Prof Location", result.getHealthcareProfessionals().get(0).getProfLocation());
    // Medications
    Assert.assertEquals("Med Name", result.getMedications().get(0).getName());
    Assert.assertEquals("Med Frequency", result.getMedications().get(0).getFrequency());
    Assert.assertEquals("Med Quantity", result.getMedications().get(0).getQuantity());
    Assert.assertTrue(result.getMedications().get(0).getIsPrescribed());
    // Treatments
    Assert.assertEquals("Treatment Description", result.getTreatments().get(0).getDescription());
    Assert.assertEquals("Treatment Time", result.getTreatments().get(0).getTime());
    // Vehicles
    Assert.assertEquals("ER1", result.getVehicles().get(0).getRegistrationNumber());
    Assert.assertEquals("CAR", result.getVehicles().get(0).getTypeCode());
    Assert.assertEquals("Usage Frequency", result.getVehicles().get(0).getUsageFrequency());
    // Walking Aids
    Assert.assertEquals("PRIVATE", result.getWalkingAids().get(0).getHowProvidedCode());
    Assert.assertEquals("Aid Description", result.getWalkingAids().get(0).getDescription());
    Assert.assertEquals("Aid Usage", result.getWalkingAids().get(0).getUsage());
    // Walking Difficulty Types
    Assert.assertEquals("PAIN", result.getWalkingDifficultyTypes().get(0).getTypeCode());
    // Application
    Assert.assertEquals("ABERD", result.getLocalAuthorityCode());
    Assert.assertEquals("REPLACE", result.getAppTypeCode());
    Assert.assertTrue(result.getIsPaymentTaken());
    Instant expectedSubmission = Instant.parse("2011-01-01T03:00:00Z");
    Assert.assertEquals(expectedSubmission, result.getSubmissionDatetime());
    Assert.assertEquals("AAAAAA", result.getExistingBadgeNo());
    Assert.assertEquals("PERSON", result.getPartyCode());
    Assert.assertEquals("Contact Name", result.getContactName());
    Assert.assertEquals("Contact Building Street", result.getContactBuildingStreet());
    Assert.assertEquals("Contact Line2", result.getContactLine2());
    Assert.assertEquals("Contact Town City", result.getContactTownCity());
    Assert.assertEquals("ZZ111ZZ", result.getContactPostcode());
    Assert.assertEquals("PPN", result.getPrimaryPhoneNo());
    Assert.assertEquals("SPN", result.getSecondaryPhoneNo());
    Assert.assertEquals("Contact Email Address", result.getContactEmailAddress());
    Assert.assertEquals("Holder Name", result.getHolderName());
    Assert.assertTrue(result.getOrgIsCharity());
    Assert.assertEquals("Org Charity No", result.getOrgCharityNo());
    Assert.assertEquals(new Integer(1), result.getNoOfBadges());
    Assert.assertEquals("Nino", result.getNino());
    Assert.assertEquals(LocalDate.parse("1970-05-29"), result.getDob());
    Assert.assertEquals("MALE", result.getGenderCode());
    Assert.assertEquals("Holder Name At Birth", result.getHolderNameAtBirth());
    Assert.assertEquals("DLA", result.getEligibilityCode());
    Assert.assertEquals("Eligibility Conditions", result.getEligibilityConditions());
    Assert.assertTrue(result.getBenefitIsIndefinite());
    Assert.assertEquals(LocalDate.parse("2020-01-31"), result.getBenefitExpiryDate());
    Assert.assertEquals("Walk Other Desc", result.getWalkOtherDesc());
    Assert.assertEquals("LESSMIN", result.getWalkLengthCode());
    Assert.assertEquals("SLOW", result.getWalkSpeedCode());
    Assert.assertEquals("Arms Driving Freq", result.getArmsDrivingFreq());
    Assert.assertTrue(result.getArmsIsAdaptedVehicle());
    Assert.assertEquals("Arms Adapted Veh Desc", result.getArmsAdaptedVehDesc());
    Assert.assertEquals("BIRM", result.getBlindRegisteredAtLaCode());
    Assert.assertEquals("BULK", result.getBulkyEquipmentTypeCode());
    Assert.assertEquals("Url Proof Eligibility", result.getUrlProofEligibility());
    Assert.assertEquals("Url Proof Address", result.getUrlProofAddress());
    Assert.assertEquals("Url Proof Identity", result.getUrlProofIdentity());
    Assert.assertEquals("Url Badge Photo", result.getUrlBadgePhoto());
  }
}
