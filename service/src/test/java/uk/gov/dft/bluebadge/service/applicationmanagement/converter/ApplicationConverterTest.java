package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.ExpectedCount.once;

import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.converter.ToEntityFormatter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class ApplicationConverterTest extends ApplicationFixture {

  ApplicationConverter converter;
  Application app;

  @Mock
  private PersonConverter personConverter;
  @Mock
  private OrganisationConverter organisationConverter;
  @Mock
  private EligibilityConverter eligibilityConverter;

  public ApplicationConverterTest() {
    super();
    converter =
        new ApplicationConverter(eligibilityConverter, organisationConverter, personConverter);
  }

  @Test
  public void convertToEntity() {
    app = getApplicationBuilder().addBaseApplication().setPerson().build();

    ApplicationEntity entity = converter.convertToEntity(app);

    // Check some values
    assertEquals(ToEntityFormatter.postcode(ValidValues.POSTCODE), entity.getContactPostcode());
    assertEquals(ValidValues.APP_TYPE_CODE.toString(), entity.getAppTypeCode());

    // Check child objects dealt with
    verify(personConverter, times(1)).convertToEntity(eq(app), any());
    verify(organisationConverter, times(1)).convertToEntity(eq(app), any());
    verify(eligibilityConverter, times(1)).convertToEntity(eq(app), any());
  }
}
