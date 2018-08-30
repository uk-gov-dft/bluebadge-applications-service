package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.HealthcareProfessionalConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.BenefitConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.BlindConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.ChildUnder3Converter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.DisabilityArmsConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility.WalkingDifficultyConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class EligibilityConverterTest extends ApplicationFixture {

  @Mock private HealthcareProfessionalConverter healthcareProfessionalConverter;
  @Mock private ChildUnder3Converter childUnder3Converter;
  @Mock private BlindConverter blindConverter;
  @Mock private DisabilityArmsConverter disabilityArmsConverter;
  @Mock private WalkingDifficultyConverter walkingDifficultyConverter;
  @Mock private BenefitConverter benefitConverter;

  private EligibilityConverter converter;

  public EligibilityConverterTest() {
    MockitoAnnotations.initMocks(this);
    converter =
        new EligibilityConverter(
            childUnder3Converter,
            blindConverter,
            disabilityArmsConverter,
            walkingDifficultyConverter,
            benefitConverter,
            healthcareProfessionalConverter);
  }

  @Test
  public void convertToEntity_NullEligibility() {
    // Given no eligibility
    Application emptyApp = new Application();

    // When convert called
    converter.convertToEntity(emptyApp, ApplicationEntity.builder().build());

    // Nothing happens
  }

  @Test
  public void convertToEntity() {
    Application model =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    // When converted
    converter.convertToEntity(model, entity);

    assertEquals(
        ValidValues.DESCRIPTION_OF_CONDITIONS, model.getEligibility().getDescriptionOfConditions());
    assertEquals(model.getEligibility().getTypeCode().name(), entity.getEligibilityCode());

    verify(healthcareProfessionalConverter, times(1))
        .convertToEntityList(model.getEligibility().getHealthcareProfessionals(), ValidValues.UUID);
    verify(childUnder3Converter, times(1)).convertToEntity(model, entity);
    verify(blindConverter, times(1)).convertToEntity(model, entity);
    verify(disabilityArmsConverter, times(1)).convertToEntity(model, entity);
    verify(walkingDifficultyConverter, times(1)).convertToEntity(model, entity);
    verify(benefitConverter, times(1)).convertToEntity(model, entity);
  }

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.DLA.name());
    Application model = new Application();

    converter.convertToModel(model, entity);

    assertEquals(
        ValidValues.DESCRIPTION_OF_CONDITIONS, model.getEligibility().getDescriptionOfConditions());
    assertEquals(EligibilityCodeField.DLA, model.getEligibility().getTypeCode());

    verify(healthcareProfessionalConverter, times(1))
        .convertToModelList(entity.getHealthcareProfessionals());
    verify(childUnder3Converter, times(1)).convertToModel(model, entity);
    verify(blindConverter, times(1)).convertToModel(model, entity);
    verify(disabilityArmsConverter, times(1)).convertToModel(model, entity);
    verify(walkingDifficultyConverter, times(1)).convertToModel(model, entity);
    verify(benefitConverter, times(1)).convertToModel(model, entity);
  }
}
