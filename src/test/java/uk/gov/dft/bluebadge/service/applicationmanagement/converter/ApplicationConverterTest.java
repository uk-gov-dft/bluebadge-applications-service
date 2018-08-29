package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ApplicationConverterTest extends ApplicationFixture {

  private ApplicationConverter converter;

  @Mock
  private PartyConverter partyConverter;
  @Mock
  private EligibilityConverter eligibilityConverter;

  public ApplicationConverterTest() {
    MockitoAnnotations.initMocks(this);
    converter =
        new ApplicationConverter(eligibilityConverter, partyConverter);
  }

  @Test
  public void convertToEntity() {
    app = getApplicationBuilder().addBaseApplication().build();

    ApplicationEntity entity = converter.convertToEntity(app);

    assertEquals(ValidValues.ID, entity.getId().toString());
    assertEquals(ValidValues.APP_TYPE_CODE.toString(), entity.getAppTypeCode());
    assertEquals(ValidValues.LA_CODE, entity.getLocalAuthorityCode());
    assertEquals(ValidValues.PAYMENT_TAKEN, entity.getIsPaymentTaken());
    assertNotNull(entity.getSubmissionDatetime());
    assertEquals(ValidValues.EXISTING_BADGE_NO, entity.getExistingBadgeNo());

    verify(partyConverter, times(1)).convertToEntity(eq(app), any());
    verify(eligibilityConverter, times(1)).convertToEntity(eq(app), any());
  }

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model = converter.convertToModel(entity);

    assertEquals(ValidValues.ID, model.getApplicationId());
    assertEquals(ValidValues.APP_TYPE_CODE, model.getApplicationTypeCode());
    assertEquals(ValidValues.LA_CODE, model.getLocalAuthorityCode());
    assertEquals(ValidValues.PAYMENT_TAKEN, model.getPaymentTaken());
    assertEquals(ValidValues.EXISTING_BADGE_NO, model.getExistingBadgeNumber());

    verify(partyConverter, times(1)).convertToModel(any(), eq(entity));
    verify(eligibilityConverter, times(1)).convertToModel(any(), eq(entity));
  }
}
