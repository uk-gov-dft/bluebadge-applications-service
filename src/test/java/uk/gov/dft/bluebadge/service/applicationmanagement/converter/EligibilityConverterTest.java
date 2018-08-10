package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.HealthcareProfessionalConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.MedicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.TreatmentConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingAidConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingDifficultyTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class EligibilityConverterTest extends ApplicationFixture {

  @Mock private WalkingDifficultyTypeConverter walkingDifficultyTypeConverter;
  @Mock private WalkingAidConverter walkingAidConverter;
  @Mock private TreatmentConverter treatmentConverter;
  @Mock private MedicationConverter medicationConverter;
  @Mock private HealthcareProfessionalConverter healthcareProfessionalConverter;

  private EligibilityConverter converter;
  private Application application;
  private ApplicationEntity entity;

  public EligibilityConverterTest() {
    super();
    converter =
        new EligibilityConverter(
            walkingDifficultyTypeConverter,
            walkingAidConverter,
            treatmentConverter,
            medicationConverter,
            healthcareProfessionalConverter);
  }

  @Before
  public void setUp() {
    entity = ApplicationEntity.builder().id(UUID.randomUUID()).build();
  }

  @Test
  public void convertToEntity_NullEligibility() {

    // Given no eligibility
    Application emptyApp = new Application();

    // When convert called
    converter.convertToEntity(emptyApp, entity);

    // Nothing happens
  }

  @Test
  public void convertToEntity_BasicValues() {
    // Given no child objects of eligibility
    application = getApplicationBuilder().addBaseApplication().setPerson().build();
    application.setEligibility(new Eligibility());
    application.getEligibility().setTypeCode(EligibilityCodeField.PIP);

    // When converted
    converter.convertToEntity(application, entity);

    // No null pointers
    assertEquals(application.getEligibility().getTypeCode().name(), entity.getEligibilityCode());
  }

  @Test
  public void convertRootObjects() {
    // Given a valid application with eligibility
    application =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityChildBulk()
            .addHealthcarePro()
            .build();

    // When converted
    converter.convertRootObjects(application.getEligibility(), entity);

    // Entity populated
    assertEquals(EligibilityCodeField.CHILDBULK.name(), entity.getEligibilityCode());
    verify(healthcareProfessionalConverter, times(1))
        .convertToEntityList(
            application.getEligibility().getHealthcareProfessionals(), entity.getId());
  }

  @Test
  public void convertChildUnder3() {
    // Given a valid child bulk app
    application = getApplicationBuilder().addBaseApplication().setEligibilityChildBulk().build();

    // When converted
    converter.convertChildUnder3(application.getEligibility(), entity);

    // Entity populated
    assertEquals(ValidValues.CHILD_BULK_EQUIP.name(), entity.getBulkyEquipmentTypeCode());
  }

  @Test
  public void convertBlind() {
    // Given a valid blind app
    application = getApplicationBuilder().addBaseApplication().setEligibilityBlind().build();

    // When converted
    converter.convertBlind(application.getEligibility(), entity);

    // Entity populated
    assertEquals(ValidValues.LA_CODE, entity.getBlindRegisteredAtLaCode());
  }

  @Test
  public void convertDisabilityArms() {
    // Given a valid arms app
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityArms().build();

    // When converted
    converter.convertDisabilityArms(application.getEligibility(), entity);

    // Entity populated
    assertEquals(ValidValues.ARMS_DRIVE_FREQ, entity.getArmsDrivingFreq());
    assertEquals(ValidValues.ARMS_IS_ADAPTED, entity.getArmsIsAdaptedVehicle());
    assertEquals(ValidValues.ARMS_ADAPTED_DESC, entity.getArmsAdaptedVehDesc());
  }

  @Test
  public void convertWalkingDifficulty() {
    // Given a valid walking difficulty app
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();

    // When converted
    converter.convertWalkingDifficulty(application.getEligibility(), entity);

    // Then entity is populated
    verify(walkingDifficultyTypeConverter, times(1)).convertToEntityList(any(), any());
    verify(walkingAidConverter, times(1)).convertToEntityList(any(), any());
    verify(treatmentConverter, times(1)).convertToEntityList(any(), any());
    verify(medicationConverter, times(1)).convertToEntityList(any(), any());

    assertEquals(ValidValues.WALKING_LENGTH_OF_TIME_CODE_FIELD.name(), entity.getWalkLengthCode());
    assertEquals(ValidValues.WALKING_SPEED_CODE_FIELD.name(), entity.getWalkSpeedCode());
  }

  @Test
  public void convertBenefit() {
    // Given an application with benefit object.
    application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();

    // When converted
    converter.convertBenefit(application.getEligibility(), entity);

    // Then benefit converted
    assertEquals(ValidValues.BENEFIT_EXPIRY, entity.getBenefitExpiryDate());
  }
}
