package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.time.ZoneOffset;
import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.*;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;

public class ApplicationSummaryConverter
    implements ToModelConverter<ApplicationSummaryEntity, ApplicationSummary> {
  @Override
  public ApplicationSummary convertToModel(ApplicationSummaryEntity entity) {
    ApplicationSummary model = new ApplicationSummary();
    model.setApplicationId(entity.getApplicationId().toString());
    model.setApplicationTypeCode(
        ApplicationTypeCodeField.fromValue(entity.getApplicationTypeCode()));
    model.setEligibilityCode(EligibilityCodeField.fromValue(entity.getEligibilityCode()));
    model.setName(entity.getHolderName());
    model.setNino(entity.getNino());
    model.setDob(entity.getDob());
    if (null != entity.getSubmissionDate()) {
      model.setSubmissionDate(entity.getSubmissionDate().atOffset(ZoneOffset.UTC));
    }
    model.setPartyTypeCode(PartyTypeCodeField.fromValue(entity.getPartyTypeCode()));
    model.setApplicationStatus(ApplicationStatusField.fromValue(entity.getApplicationStatus()));
    return model;
  }
}
