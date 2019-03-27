package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationStatusField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

/** Converts an ALREADY validated entity to/from model. */
@Component
public class ApplicationConverter
    implements ToEntityConverter<ApplicationEntity, Application>,
        ToModelConverter<ApplicationEntity, Application> {

  private final ArrayList<ApplicationBiConverter> converters = new ArrayList<>();

  @Autowired
  ApplicationConverter(EligibilityConverter eligibilityConverter, PartyConverter partyConverter) {
    converters.add(eligibilityConverter);
    converters.add(partyConverter);
  }

  @Override
  public ApplicationEntity convertToEntity(Application model) {
    // If creating an application pre populate the id.
    Assert.notNull(
        model.getApplicationId(), "Before converting Application, must set applicationId.");
    Assert.notNull(
        model.getApplicationTypeCode(),
        "Before converting Application validation should have failed if typeCode is null.");

    // Populate application root fields
    ApplicationEntity entity =
        ApplicationEntity.builder()
            .appTypeCode(model.getApplicationTypeCode().toString())
            .id(UUID.fromString(model.getApplicationId()))
            .localAuthorityCode(model.getLocalAuthorityCode())
            .isPaymentTaken(model.getPaymentTaken())
            .paymentReference((model.getPaymentReference()))
            .submissionDatetime(
                null == model.getSubmissionDate() ? null : model.getSubmissionDate().toInstant())
            .existingBadgeNo(model.getExistingBadgeNumber())
            .applicationStatus(
                model.getApplicationStatus() == null
                    ? ApplicationStatusField.TODO.name()
                    : model.getApplicationStatus().name())
            .build();

    for (ApplicationBiConverter converter : converters) {
      converter.convertToEntity(model, entity);
    }

    return entity;
  }

  @Override
  public Application convertToModel(ApplicationEntity entity) {
    Application model = new Application();
    model.setApplicationTypeCode(ApplicationTypeCodeField.fromValue(entity.getAppTypeCode()));
    model.setApplicationId(entity.getId().toString());
    model.setLocalAuthorityCode(entity.getLocalAuthorityCode());
    model.setPaymentTaken(entity.getIsPaymentTaken());
    model.setPaymentReference(entity.getPaymentReference());
    model.setSubmissionDate(entity.getSubmissionDatetime().atOffset(ZoneOffset.UTC));
    model.setExistingBadgeNumber(entity.getExistingBadgeNo());
    model.setApplicationStatus(ApplicationStatusField.fromValue(entity.getApplicationStatus()));
    model.setTransferredFromLaDate(
        null != entity.getTransferredFromLaDatetime()
            ? entity.getTransferredFromLaDatetime().atOffset(ZoneOffset.UTC)
            : null);
    model.setTransferredLaFromCode(entity.getTransferredLaFromCode());
    model.setArtifacts(null);

    for (ApplicationBiConverter converter : converters) {
      converter.convertToModel(model, entity);
    }

    return model;
  }
}
