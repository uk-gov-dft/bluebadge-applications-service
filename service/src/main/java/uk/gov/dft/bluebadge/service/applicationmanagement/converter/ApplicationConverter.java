package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.common.converter.ToEntityFormatter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Contact;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import java.util.UUID;

/**
 * Converts an ALREADY validated entity to/from model.
 */
@Component
public class ApplicationConverter implements ToEntityConverter<ApplicationEntity, Application> {

  private final EligibilityConverter eligibilityConverter;
  private final OrganisationConverter organisationConverter;
  private final PersonConverter personConverter;

  @Autowired
  ApplicationConverter(
      EligibilityConverter eligibilityConverter,
      OrganisationConverter organisationConverter,
      PersonConverter personConverter) {
    this.eligibilityConverter = eligibilityConverter;
    this.organisationConverter = organisationConverter;
    this.personConverter = personConverter;
  }

  @Override
  public ApplicationEntity convertToEntity(Application application) {
    // If creating an application pre populate the id.
    Assert.notNull(application.getApplicationId(), "Before converting Application, must set applicationId.");
    // The following are NotNull annotated in beans and validation should have been done.
    Assert.notNull(application.getParty(), "Before converting Application validation should have failed for null party.");
    Assert.notNull(application.getParty().getContact(), "Before converting Application validation should have failed for null contact.");
    Assert.notNull(application.getApplicationTypeCode(), "Before converting Application validation should have failed if typeCode is null.");

    Contact contact = application.getParty().getContact();

    // Populate application root fields
    ApplicationEntity entity =
        ApplicationEntity.builder()
            .appTypeCode(application.getApplicationTypeCode().toString())
            .id(UUID.fromString(application.getApplicationId()))
            .localAuthorityCode(application.getLocalAuthorityCode())
            .isPaymentTaken(application.isIsPaymentTaken())
            .submissionDatetime(application.getSubmissionDate())
            .existingBadgeNo(application.getExistingBadgeNumber())
            .contactName(contact.getFullName())
            .contactBuildingStreet(contact.getBuildingStreet())
            .contactLine2(contact.getLine2())
            .contactTownCity(contact.getTownCity())
            .contactEmailAddress(contact.getEmailAddress())
            .contactPostcode(ToEntityFormatter.postcode(contact.getPostCode()))
            .primaryPhoneNo(contact.getPrimaryPhoneNumber())
            .secondaryPhoneNo(contact.getSecondaryPhoneNumber())
            .partyCode(application.getParty().getTypeCode().toString())
            .build();

    personConverter.convertToEntity(application, entity);
    eligibilityConverter.convertToEntity(application, entity);
    organisationConverter.convertToEntity(application, entity);

    return entity;
  }
}
