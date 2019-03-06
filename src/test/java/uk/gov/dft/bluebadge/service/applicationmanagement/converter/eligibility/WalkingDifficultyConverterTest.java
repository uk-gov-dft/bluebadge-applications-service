package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficulty;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.BreathlessnessTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.MedicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.TreatmentConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingAidConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.WalkingDifficultyTypeConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class WalkingDifficultyConverterTest extends ApplicationFixture {

  private final WalkingDifficultyConverter converter;

  @Mock WalkingDifficultyTypeConverter walkingDifficultyTypeConverter;
  @Mock WalkingAidConverter walkingAidConverter;
  @Mock TreatmentConverter treatmentConverter;
  @Mock MedicationConverter medicationConverter;
  @Mock BreathlessnessTypeConverter breathlessnessTypeConverter;

  public WalkingDifficultyConverterTest() {
    MockitoAnnotations.initMocks(this);
    converter =
        new WalkingDifficultyConverter(
            walkingDifficultyTypeConverter,
            walkingAidConverter,
            treatmentConverter,
            medicationConverter,
            breathlessnessTypeConverter);
  }

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.WALKD.name());
    Application model = new Application();
    converter.convertToModel(model, entity);

    WalkingDifficulty walkingDifficulty = model.getEligibility().getWalkingDifficulty();

    assertEquals(
        ValidValues.WALKING_LENGTH_OF_TIME_CODE_FIELD,
        walkingDifficulty.getWalkingLengthOfTimeCode());
    assertEquals(ValidValues.WALKING_SPEED_CODE_FIELD, walkingDifficulty.getWalkingSpeedCode());
    assertEquals(ValidValues.WALK_OTHER_DESC, walkingDifficulty.getOtherDescription());

    verify(walkingDifficultyTypeConverter, times(1))
        .convertToModelList(entity.getWalkingDifficultyTypes());
    verify(walkingAidConverter, times(1)).convertToModelList(entity.getWalkingAids());
    verify(treatmentConverter, times(1)).convertToModelList(entity.getTreatments());
    verify(medicationConverter, times(1)).convertToModelList(entity.getMedications());
  }

  @Test
  public void convertToEntity() {
    Application model =
        getApplicationBuilder().addBaseApplication().setEligibilityWalking().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    WalkingDifficulty walkingDifficulty = model.getEligibility().getWalkingDifficulty();
    converter.convertToEntity(model, entity);

    assertEquals(
        ApplicationFixture.ValidValues.WALKING_LENGTH_OF_TIME_CODE_FIELD.name(),
        entity.getWalkLengthCode());
    assertEquals(
        ApplicationFixture.ValidValues.WALKING_SPEED_CODE_FIELD.name(), entity.getWalkSpeedCode());
    assertEquals(ApplicationFixture.ValidValues.WALK_OTHER_DESC, entity.getWalkOtherDesc());

    verify(walkingDifficultyTypeConverter, times(1))
        .convertToEntityList(walkingDifficulty.getTypeCodes(), UUID.fromString(ValidValues.ID));
    verify(walkingAidConverter, times(1))
        .convertToEntityList(walkingDifficulty.getWalkingAids(), UUID.fromString(ValidValues.ID));
    verify(treatmentConverter, times(1))
        .convertToEntityList(walkingDifficulty.getTreatments(), UUID.fromString(ValidValues.ID));
    verify(medicationConverter, times(1))
        .convertToEntityList(walkingDifficulty.getMedications(), UUID.fromString(ValidValues.ID));
  }
}
