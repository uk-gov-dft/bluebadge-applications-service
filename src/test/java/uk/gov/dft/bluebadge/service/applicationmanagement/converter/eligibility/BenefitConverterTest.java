package uk.gov.dft.bluebadge.service.applicationmanagement.converter.eligibility;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Benefit;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class BenefitConverterTest extends ApplicationFixture {

  private final BenefitConverter converter = new BenefitConverter();

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    entity.setEligibilityCode(EligibilityCodeField.WPMS.name());
    Application model = new Application();
    converter.convertToModel(model, entity);

    Benefit benefit = model.getEligibility().getBenefit();
    assertEquals(ValidValues.BENEFIT_EXPIRY, benefit.getExpiryDate());
    assertEquals(ValidValues.BENEFIT_IS_INDEFINITE, benefit.isIsIndefinite());
  }

  @Test
  public void convertToEntity() {
    Application model = getApplicationBuilder().addBaseApplication().setEligibilityWpms().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    converter.convertToEntity(model, entity);

    assertEquals(ValidValues.BENEFIT_EXPIRY, entity.getBenefitExpiryDate());
    assertEquals(ValidValues.BENEFIT_IS_INDEFINITE, entity.getBenefitIsIndefinite());
  }
}
