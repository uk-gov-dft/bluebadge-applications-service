package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model.ApplicationSubmittedMessageRequest;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

public class MessageServiceTest extends ApplicationFixture {
  MessageService messageService;
  @Mock private ReferenceDataService referenceDataServiceMock;
  @Mock private MessageApiClient messageApiClientMock;

  @Before
  public void setup() {
    initMocks(this);
    messageService = new MessageService(referenceDataServiceMock, messageApiClientMock);
  }

  @Test
  public void selfApplication() {
    LocalAuthorityRefData la = new LocalAuthorityRefData();
    la.setShortCode("LA");
    la.setDescription("LA Name");
    when(referenceDataServiceMock.getLocalAuthority(any())).thenReturn(la);
    when(referenceDataServiceMock.getEligibilityDescription(any())).thenReturn("Elig Desc");

    Application testApplication =
        getApplicationBuilder().addBaseApplication().setEligibilityDla().setPerson().build();

    setAllArtifacts(testApplication);

    messageService.sendApplicationSubmittedMessage(testApplication);

    ArgumentCaptor<ApplicationSubmittedMessageRequest> captor =
        ArgumentCaptor.forClass(ApplicationSubmittedMessageRequest.class);
    verify(messageApiClientMock).sendApplicationSubmittedMessage(captor.capture());

    ApplicationSubmittedMessageRequest messageRequest = captor.getValue();
    assertThat(messageRequest).isNotNull();
    assertThat(messageRequest.getFullName()).isEqualTo(ValidValues.CONTACT_NAME);
    assertThat(messageRequest.getEligibilityCodeField()).isEqualTo(EligibilityCodeField.DLA);
    assertThat(messageRequest.getEligibilityCriteria()).isEqualTo("Elig Desc");
  }

  @Test
  public void someoneElseApplication() {
    LocalAuthorityRefData la = new LocalAuthorityRefData();
    la.setShortCode("LA");
    la.setDescription("LA Name");
    when(referenceDataServiceMock.getLocalAuthority(any())).thenReturn(la);
    when(referenceDataServiceMock.getEligibilityDescription(any())).thenReturn("Elig Desc");

    Application testApplication =
        getApplicationBuilder().addBaseApplication().setEligibilityPip().setPerson().build();
    setAllArtifacts(testApplication);
    testApplication.getParty().getContact().setFullName(null);

    messageService.sendApplicationSubmittedMessage(testApplication);

    ArgumentCaptor<ApplicationSubmittedMessageRequest> captor =
        ArgumentCaptor.forClass(ApplicationSubmittedMessageRequest.class);
    verify(messageApiClientMock).sendApplicationSubmittedMessage(captor.capture());

    ApplicationSubmittedMessageRequest messageRequest = captor.getValue();
    assertThat(messageRequest).isNotNull();
    assertThat(messageRequest.getFullName()).isEqualTo(ValidValues.BADGE_HOLDER_NAME);
    assertThat(messageRequest.getEligibilityCodeField()).isEqualTo(EligibilityCodeField.PIP);
    assertThat(messageRequest.getEligibilityCriteria()).isEqualTo("Elig Desc");
  }
}
