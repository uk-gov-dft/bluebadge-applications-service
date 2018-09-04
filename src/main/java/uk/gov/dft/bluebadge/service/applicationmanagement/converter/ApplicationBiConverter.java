package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Eligibility;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public interface ApplicationBiConverter {

  void convertToModel(Application model, ApplicationEntity entity);

  void convertToEntity(Application model, ApplicationEntity entity);

  default boolean isPersonApplication(ApplicationEntity entity) {
    return PartyTypeCodeField.PERSON.name().equals(entity.getPartyCode());
  }

  default void ensureHasEligibility(Application application) {
    if (null == application.getEligibility()) {
      application.setEligibility(new Eligibility());
    }
  }
}
