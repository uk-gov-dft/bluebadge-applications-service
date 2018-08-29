package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BlindValidatorTest extends ApplicationFixture {

  @Mock
  protected ReferenceDataApiClient referenceDataApiClient;

  private ReferenceDataService referenceDataService;
  private BlindValidator validator;

  public BlindValidatorTest() {
    MockitoAnnotations.initMocks(this);
    referenceDataService = initValidRefData(referenceDataApiClient);
    this.validator = new BlindValidator(referenceDataService);
  }

  @Test
  public void validate() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build());
    validator.validate(app, errors);
    assertEquals(0, errors.getErrorCount());

    app.getEligibility().getBlind().setRegisteredAtLaId("ABC");
    reset();
    validator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BLIND_REG_AT_LA));
  }

  @Test
  public void validate_nulls() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityTermIll().build());
    validator.validate(app, errors);
  }
}
