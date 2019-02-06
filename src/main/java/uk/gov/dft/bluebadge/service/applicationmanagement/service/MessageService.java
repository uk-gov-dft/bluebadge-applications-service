package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model.ApplicationSubmittedMessageRequest;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

@Slf4j
@Service
@Transactional
public class MessageService {

  private final ReferenceDataService referenceDataService;
  private final MessageApiClient messageApiClient;

  @Autowired
  MessageService(ReferenceDataService referenceDataService, MessageApiClient messageApiClient) {
    this.referenceDataService = referenceDataService;
    this.messageApiClient = messageApiClient;
  }

  public void sendApplicationSubmittedMessage(Application application) {
    LocalAuthorityRefData localAuthority =
        referenceDataService.getLocalAuthority(application.getLocalAuthorityCode());

    EligibilityCodeField eligibility = application.getEligibility().getTypeCode();

    String contactName = application.getParty().getContact().getFullName();
    contactName =
        StringUtils.isBlank(contactName)
            ? application.getParty().getPerson().getBadgeHolderName()
            : contactName;

    ApplicationSubmittedMessageRequest messageReq =
        ApplicationSubmittedMessageRequest.builder()
            .laShortCode(application.getLocalAuthorityCode())
            .fullName(contactName)
            .emailAddress(application.getParty().getContact().getEmailAddress())
            .localAuthority(localAuthority.getDescription())
            .eligibility(eligibility)
            .eligibilityCriteria(referenceDataService.getEligibilityDescription(eligibility.name()))
            .dob(application.getParty().getPerson().getDob())
            .providedArtifacts(
                application
                    .getArtifacts()
                    .stream()
                    .map(Artifact::getType)
                    .collect(Collectors.toSet()))
            .paymentTaken(application.getPaymentTaken())
            .build();

    try {
      messageApiClient.sendApplicationSubmittedMessage(messageReq);
    } catch (Exception e) {
      log.warn("Failed to send 'Application Submitted' message.", e);
    }
  }
}
