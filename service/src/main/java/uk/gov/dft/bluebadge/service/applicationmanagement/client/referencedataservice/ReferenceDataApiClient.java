package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.RestTemplateFactory;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceDataResponse;

@Slf4j
@Service
public class ReferenceDataApiClient {

  private final ReferenceDataServiceConfiguration messageServiceConfiguration;
  private final RestTemplateFactory restTemplateFactory;

  @Autowired
  public ReferenceDataApiClient(
      ReferenceDataServiceConfiguration serviceConfiguration,
      RestTemplateFactory restTemplateFactory) {
    this.messageServiceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  /**
   * Retrieve badge reference data
   *
   * @return List of reference data items.
   */
  public List<ReferenceData> retrieveReferenceData(String domain) {
    log.debug("Loading reference data.");

    ReferenceDataResponse response =
        restTemplateFactory
            .getInstance()
            .getForEntity(
                messageServiceConfiguration
                    .getUriComponentsBuilder("reference-data", domain)
                    .toUriString(),
                ReferenceDataResponse.class)
            .getBody();

    return response.getData();
  }
}
