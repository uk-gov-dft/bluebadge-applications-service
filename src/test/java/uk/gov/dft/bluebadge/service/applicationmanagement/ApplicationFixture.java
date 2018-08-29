package uk.gov.dft.bluebadge.service.applicationmanagement;

import com.google.common.collect.Lists;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Benefit;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Blind;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ChildUnder3;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.DisabilityArms;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.GenderCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Person;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Vehicle;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.VehicleTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingSpeedCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.AbstractValidator;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ApplicationFixture extends AbstractValidator {

  protected interface ValidValues {
    String LA_CODE = "BIRM";
    String BADGE_HOLDER_NAME = "Harry Bloggs";
    String BADGE_HOLDER_NAME_AT_BIRTH = "Harry Bloggs The Third";
    Boolean IS_CHARITY = Boolean.TRUE;
    String CHARITY_NO = "123456";
    Integer NO_OF_BADGES = 1;
    LocalDate DOB = LocalDate.now().minus(Period.ofYears(30));
    GenderCodeField GENDER = GenderCodeField.MALE;
    String NINO = "ns 12 34 56A";
    String NINO_FORMATTED = "NS123456A";
    LocalDate BENEFIT_EXPIRY = LocalDate.now().plus(Period.ofYears(1));
    Boolean BENEFIT_IS_INDEFINITE = Boolean.FALSE;
    String PROFESSIONAL_NAME = "pro name";
    String PROFESSIONAL_LOCATION = "pro location";
    String VEH_REG = "VK61VXX";
    String VEH_USAGE = "veh usage";
    VehicleTypeCodeField VEH_TYPE = VehicleTypeCodeField.CAR;
    List<WalkingDifficultyTypeCodeField> WALKING_DIFFICULTY_TYPE_CODES =
        Lists.newArrayList(WalkingDifficultyTypeCodeField.BALANCE);
    WalkingLengthOfTimeCodeField WALKING_LENGTH_OF_TIME_CODE_FIELD =
        WalkingLengthOfTimeCodeField.FEWMIN;
    WalkingSpeedCodeField WALKING_SPEED_CODE_FIELD = WalkingSpeedCodeField.FAST;
    String ARMS_DRIVE_FREQ = "drive freq";
    Boolean ARMS_IS_ADAPTED = Boolean.TRUE;
    String PHONE_NO = "123456";
    String CONTACT_BUILDING = "29 A Street";
    String CONTACT_EMAIL = "a@b.c";
    String CONTACT_NAME = "Mr Contact";
    String LINE2 = "Town Area";
    String POSTCODE = "WV16 4Aw";
    String POSTCODE_FORMATTED = "WV164AW";
    String TOWN = "Arlington";
    ApplicationTypeCodeField APP_TYPE_CODE = ApplicationTypeCodeField.NEW;
    String ARMS_ADAPTED_DESC = "Vehicle description";
    BulkyMedicalEquipmentTypeCodeField CHILD_BULK_EQUIP = BulkyMedicalEquipmentTypeCodeField.CAST;
    String ID = "75a0d22f-8daf-4217-b1de-6d4136612a1d";
    Boolean PAYMENT_TAKEN = Boolean.TRUE;
    String EXISTING_BADGE_NO = "ABCDEF";
    String PARTY = "PERSON";
  }

  protected BeanPropertyBindingResult errors;
  protected Application app;

  protected ApplicationBuilder getApplicationBuilder() {
    return new ApplicationBuilder();
  }

  protected ReferenceDataService initValidRefData(ReferenceDataApiClient mockRefDataClient) {
    List<ReferenceData> referenceDataList = new ArrayList<>();

    for (RefDataGroupEnum group : RefDataGroupEnum.values()) {
      if (null != group.getEnumClass()) {
        addRefDataGroup(referenceDataList, group.getGroupKey(), group.getEnumClass());
      }
    }
    referenceDataList.add(getNewRefDataItem("LA", "BIRM", "Birmingham"));

    when(mockRefDataClient.retrieveReferenceData(any())).thenReturn(referenceDataList);
    return new ReferenceDataService(mockRefDataClient);
  }

  private void addRefDataGroup(
      List<ReferenceData> list, String groupCode, Class<? extends Enum<?>> model) {
    for (Enum e : model.getEnumConstants()) {
      list.add(getNewRefDataItem(groupCode, e.name(), e.name()));
    }
  }

  private ReferenceData getNewRefDataItem(String group, String key, String description) {
    return ReferenceData.builder()
        .groupShortCode(group)
        .shortCode(key)
        .description(description)
        .displayOrder(1)
        .groupDescription(null)
        .subgroupDescription(null)
        .subgroupShortCode(null)
        .build();
  }

  protected static void addPerson(Application application) {
    Person person = new Person();
    person.setBadgeHolderName(ValidValues.BADGE_HOLDER_NAME);
    person.setDob(ValidValues.DOB);
    person.setGenderCode(ValidValues.GENDER);
    person.setNameAtBirth(ValidValues.BADGE_HOLDER_NAME_AT_BIRTH);
    person.setNino(ValidValues.NINO);
    application.getParty().setPerson(person);
  }

  protected static void addBenefit(Application application) {
    Benefit benefit = new Benefit();
    benefit.setExpiryDate(ValidValues.BENEFIT_EXPIRY);
    benefit.setIsIndefinite(ValidValues.BENEFIT_IS_INDEFINITE);
    application.getEligibility().setBenefit(benefit);
  }

  protected static void addWalking(Application application) {
    WalkingDifficulty walkingDifficulty = new WalkingDifficulty();
    walkingDifficulty.setTypeCodes(ValidValues.WALKING_DIFFICULTY_TYPE_CODES);
    walkingDifficulty.setWalkingLengthOfTimeCode(ValidValues.WALKING_LENGTH_OF_TIME_CODE_FIELD);
    walkingDifficulty.setWalkingSpeedCode(ValidValues.WALKING_SPEED_CODE_FIELD);
    application.getEligibility().setWalkingDifficulty(walkingDifficulty);
  }

  protected static void addChild(Application application) {
    ChildUnder3 child = new ChildUnder3();
    child.setBulkyMedicalEquipmentTypeCode(ValidValues.CHILD_BULK_EQUIP);
    application.getEligibility().setChildUnder3(child);
  }

  protected static void addArms(Application application) {
    DisabilityArms arms = new DisabilityArms();
    arms.setDrivingFrequency(ValidValues.ARMS_DRIVE_FREQ);
    arms.setIsAdaptedVehicle(ValidValues.ARMS_IS_ADAPTED);
    arms.setAdaptedVehicleDescription(ValidValues.ARMS_ADAPTED_DESC);
    application.getEligibility().disabilityArms(arms);
  }

  protected static void addOrganisation(Application application) {
    Organisation organisation = new Organisation();
    organisation.setBadgeHolderName(ValidValues.BADGE_HOLDER_NAME);
    organisation.setNumberOfBadges(ValidValues.NO_OF_BADGES);
    organisation.setIsCharity(ValidValues.IS_CHARITY);
    organisation.setCharityNumber(ValidValues.CHARITY_NO);
    application.getParty().setOrganisation(organisation);
  }

  protected static void setEligibilityPip(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.PIP);
    addBenefit(application);
  }

  private static void setEligibilityDla(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.DLA);
    addBenefit(application);
  }

  private static void setEligibilityWpms(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.WPMS);
    addBenefit(application);
  }

  private static void setEligibilityArms(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.ARMS);
    addArms(application);
  }

  private static void setEligibilityWalking(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.WALKD);
    addWalking(application);
  }

  private static void setEligibilityChildBulk(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.CHILDBULK);
    addChild(application);
  }

  protected static void addBlind(Application application) {
    Blind blind = new Blind();
    blind.setRegisteredAtLaId(ValidValues.LA_CODE);
    application.getEligibility().setBlind(blind);
  }

  private static void setEligibilityBlind(Application application) {
    application.setEligibility(new Eligibility());
    addBlind(application);
    application.getEligibility().setTypeCode(EligibilityCodeField.BLIND);
  }

  private static void setEligibilityAfrfcs(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.AFRFCS);
  }

  private static void setEligibilityTermill(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.TERMILL);
  }

  private static void setEligibilityChildVehic(Application application) {
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.CHILDVEHIC);
  }

  protected void reset(Application application) {
    app = application;
    errors = new BeanPropertyBindingResult(application, "application");
  }

  protected void reset() {
    reset(app);
  }

  protected class ApplicationBuilder {

    Application application;

    ApplicationBuilder() {
      application = new Application();
    }

    public ApplicationBuilder addBaseApplication() {
      Party party = new Party();
      Contact contact = new Contact();
      contact.secondaryPhoneNumber(ValidValues.PHONE_NO);
      contact.setBuildingStreet(ValidValues.CONTACT_BUILDING);
      contact.setEmailAddress(ValidValues.CONTACT_EMAIL);
      contact.setFullName(ValidValues.CONTACT_NAME);
      contact.setLine2(ValidValues.LINE2);
      contact.setPostCode(ValidValues.POSTCODE);
      contact.setPrimaryPhoneNumber(ValidValues.PHONE_NO);
      contact.setTownCity(ValidValues.TOWN);
      party.setContact(contact);
      application.setParty(party);
      application.setLocalAuthorityCode(ValidValues.LA_CODE);
      application.setApplicationTypeCode(ValidValues.APP_TYPE_CODE);
      application.setApplicationId(ValidValues.ID);
      application.setPaymentTaken(ValidValues.PAYMENT_TAKEN);
      application.setExistingBadgeNumber(ValidValues.EXISTING_BADGE_NO);
      application.setSubmissionDate(OffsetDateTime.now(Clock.systemUTC()));
      return this;
    }

    public ApplicationBuilder setOrganisation() {
      application.getParty().setTypeCode(PartyTypeCodeField.ORG);
      addOrganisation(application);
      return this;
    }

    public ApplicationBuilder setPerson() {
      application.getParty().setTypeCode(PartyTypeCodeField.PERSON);
      addPerson(application);
      return this;
    }

    public ApplicationBuilder setEligibilityPip() {
      ApplicationFixture.setEligibilityPip(application);
      return this;
    }

    @SuppressWarnings("unused")
    public ApplicationBuilder setEligibilityDla() {
      ApplicationFixture.setEligibilityDla(application);
      return this;
    }

    @SuppressWarnings("unused")
    public ApplicationBuilder setEligibilityWpms() {
      ApplicationFixture.setEligibilityWpms(application);
      return this;
    }

    public Application build() {
      return application;
    }

    public ApplicationBuilder setEligibilityArms() {
      ApplicationFixture.setEligibilityArms(application);
      return this;
    }

    public ApplicationBuilder setEligibilityWalking() {
      ApplicationFixture.setEligibilityWalking(application);
      return this;
    }

    public ApplicationBuilder setEligibilityChildBulk() {
      ApplicationFixture.setEligibilityChildBulk(application);
      return this;
    }

    public ApplicationBuilder setEligibilityBlind() {
      ApplicationFixture.setEligibilityBlind(application);
      return this;
    }

    @SuppressWarnings("unused")
    public ApplicationBuilder setEligibilityChildVehicle() {
      ApplicationFixture.setEligibilityChildVehic(application);
      return this;
    }

    public ApplicationBuilder setEligibilityTermIll() {
      ApplicationFixture.setEligibilityTermill(application);
      return this;
    }

    public ApplicationBuilder setEligibilityAfrfcs() {
      ApplicationFixture.setEligibilityAfrfcs(application);
      return this;
    }

    public ApplicationBuilder addHealthcarePro() {
      if (null == application.getEligibility().getHealthcareProfessionals()) {
        application.getEligibility().setHealthcareProfessionals(new ArrayList<>());
      }
      HealthcareProfessional pro = new HealthcareProfessional();
      pro.setName(ValidValues.PROFESSIONAL_NAME);
      pro.setLocation(ValidValues.PROFESSIONAL_LOCATION);
      application.getEligibility().getHealthcareProfessionals().add(pro);
      return this;
    }

    public ApplicationBuilder addVehicle() {
      if (null == application.getParty().getOrganisation().getVehicles()) {
        application.getParty().getOrganisation().setVehicles(new ArrayList<>());
      }
      Vehicle vehicle = new Vehicle();
      vehicle.setRegistrationNumber(ValidValues.VEH_REG);
      vehicle.setUsageFrequency(ValidValues.VEH_USAGE);
      vehicle.setTypeCode(ValidValues.VEH_TYPE);
      application.getParty().getOrganisation().getVehicles().add(vehicle);
      return this;
    }
  }

  protected ApplicationEntity getFullyPopulatedApplicationEntity() {
    return ApplicationEntity.builder()
        .id(UUID.fromString(ValidValues.ID))
        .contactBuildingStreet(ValidValues.CONTACT_BUILDING)
        .contactEmailAddress(ValidValues.CONTACT_EMAIL)
        .contactLine2(ValidValues.LINE2)
        .contactName(ValidValues.CONTACT_NAME)
        .contactPostcode(ValidValues.POSTCODE)
        .contactTownCity(ValidValues.TOWN)
        .primaryPhoneNo(ValidValues.PHONE_NO)
        .secondaryPhoneNo(ValidValues.PHONE_NO)
        .partyCode(ValidValues.PARTY)
        .submissionDatetime(Instant.now())
        .appTypeCode(ValidValues.APP_TYPE_CODE.name())
        .localAuthorityCode(ValidValues.LA_CODE)
        .isPaymentTaken(ValidValues.PAYMENT_TAKEN)
        .existingBadgeNo(ValidValues.EXISTING_BADGE_NO)
        .holderName(ValidValues.BADGE_HOLDER_NAME)
        .holderNameAtBirth(ValidValues.BADGE_HOLDER_NAME_AT_BIRTH)
        .dob(ValidValues.DOB)
        .genderCode(ValidValues.GENDER.name())
        .nino(ValidValues.NINO_FORMATTED)
        .build();
  }
}
