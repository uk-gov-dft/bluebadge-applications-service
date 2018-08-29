package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public interface ApplicationBiConverter {

  void convertToModel(Application model, ApplicationEntity entity);

  void convertToEntity(Application model, ApplicationEntity entity);
}
