package uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.MessageApiClient.SEND_MESSAGE_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model.ApplicationSubmittedMessageRequest;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model.UuidResponse;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model.UuidResponseData;

public class MessageApiClientTest {
  public static final String TEST_URI = "http://justtesting:7777/test";

  private MessageApiClient client;
  private MockRestServiceServer mockServer;
  private ObjectMapper om = new ObjectMapper();
  private UuidResponse uuidResponse;

  @Before
  public void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();

    client = new MessageApiClient(restTemplate);

    UUID uuid = UUID.randomUUID();
    uuidResponse = new UuidResponse().data(new UuidResponseData().uuid(uuid.toString()));
  }

  @SneakyThrows
  @Test
  public void whenApplicationSubmitted_thenGenericMessageSent_andAttributesSet() {
    mockServer
        .expect(once(), requestTo(TEST_URI + SEND_MESSAGE_URL))
        .andExpect(method(HttpMethod.POST))
        .andExpect(jsonPath("template", equalTo("APPLICATION_SUBMITTED")))
        .andExpect(jsonPath("emailAddress", equalTo("bob@bob.com")))
        .andExpect(jsonPath("laShortCode", equalTo("LA_TEST")))
        .andExpect(jsonPath("attributes.['full name']", equalTo("bob")))
        .andExpect(jsonPath("attributes.['local authority']", equalTo("Cumbria")))
        .andRespond(withSuccess(om.writeValueAsString(uuidResponse), MediaType.APPLICATION_JSON));

    ApplicationSubmittedMessageRequest messageRequest =
        ApplicationSubmittedMessageRequest.builder()
            .emailAddress("bob@bob.com")
            .laShortCode("LA_TEST")
            .localAuthority("Cumbria")
            .fullName("bob")
            .dob(LocalDate.now().minusYears(25L))
            .eligibility(EligibilityCodeField.DLA)
            .eligibilityCriteria("Some description")
            .paymentTaken(true)
            .providedArtifacts(EnumSet.allOf(Artifact.TypeEnum.class))
            .build();
    UUID uuid = client.sendApplicationSubmittedMessage(messageRequest);

    assertThat(uuid).isNotNull();
    assertThat(uuid.toString()).isEqualTo(uuidResponse.getData().getUuid());
  }
}
