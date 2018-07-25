package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class ApplicationConverterTest extends ApplicationFixture {

  ApplicationConverter converter;
  Application app;

  public ApplicationConverterTest() {
    super();
    converter =
        new ApplicationConverter(
            new VehicleConverter(),
            new WalkingDifficultyTypeConverter(),
            new WalkingAidConverter(),
            new TreatmentConverter(),
            new MedicationConverter(),
            new HealthcareProfessionalConverter());
  }

  @Test
  public void convertToEntityOnCreate_org() {
    // Check an org with vehicle.
    app = getApplicationBuilder().addBaseApplication().setOrganisation().addVehicle().build();

    ApplicationEntity entity = converter.convertToEntity(app);
    assertEquals(1, entity.getVehicles().size());
    assertNotNull(entity.getId());
  }

  @Test
  public void convertToEntityOnCreate_person_with_benefit() {
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();

    ApplicationEntity entity = converter.convertToEntity(app);
    assertEquals(entity.getBenefitExpiryDate(), ValidValues.BENEFIT_EXPIRY);
    assertEquals(entity.getBenefitIsIndefinite(), ValidValues.BENEFIT_IS_INDEFINITE);
  }

  @Test
  public void convertToEntityOnCreate_person_with_walking_diff() {
    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();

    ApplicationEntity entity = converter.convertToEntity(app);
    assertEquals(
        ValidValues.WALKING_LENGTH_OF_TIME_CODE_FIELD.toString(), entity.getWalkLengthCode());
    assertEquals(
        ValidValues.WALKING_DIFFICULTY_TYPE_CODES.get(0).name(),
        entity.getWalkingDifficultyTypes().get(0).getTypeCode());
  }

  @Test
  public void convertToEntityOnCreate_person_arms_and_professional_and_blind() {
    app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityArms()
            .addHealthcarePro()
            .build();
    // Although invalid, ok for converter.  Would fail validation
    addBlind(app);

    ApplicationEntity entity = converter.convertToEntity(app);
    assertEquals(1, entity.getHealthcareProfessionals().size());
    assertEquals(ValidValues.ARMS_DRIVE_FREQ, entity.getArmsDrivingFreq());
    assertEquals(ValidValues.ARMS_IS_ADAPTED, entity.getArmsIsAdaptedVehicle());
    assertEquals(ValidValues.LA_CODE, entity.getBlindRegisteredAtLaCode());
  }
}
