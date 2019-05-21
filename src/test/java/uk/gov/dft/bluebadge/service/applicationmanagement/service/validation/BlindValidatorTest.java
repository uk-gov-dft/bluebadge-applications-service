package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

public class BlindValidatorTest extends ApplicationFixture {

  @Mock protected ReferenceDataApiClient referenceDataApiClient;

  private ReferenceDataService referenceDataService;
  private BlindValidator validator;

  public BlindValidatorTest() {
    MockitoAnnotations.initMocks(this);
    referenceDataService = initValidRefData(referenceDataApiClient);
    this.validator = new BlindValidator(referenceDataService);
  }

  @Test
  public void validate() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    validator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    app.getEligibility().getBlind().setRegisteredAtLaId("ABC");

    errors = getNewBindingResult(app);
    validator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BLIND_REG_AT_LA));
  }

  @Test
  public void validate_nulls() {
    Application app =
        getApplicationBuilder()
            .addBaseApplication()
            .setPerson()
            .setEligibilityChildVehicle()
            .build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    validator.validate(app, errors);
  }
}
