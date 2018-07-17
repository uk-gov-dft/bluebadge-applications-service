package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

public class HealthcareProfessionalConverter
    extends ApplicationToEntityCollection<HealthcareProfessionalEntity, HealthcareProfessional> {

  @Override
  HealthcareProfessionalEntity mapToEntity(
      HealthcareProfessional healthcareProfessional, UUID applicationId) {
    return HealthcareProfessionalEntity.builder()
        .profLocation(healthcareProfessional.getLocation())
        .profName(healthcareProfessional.getName())
        .applicationId(applicationId)
        .build();
  }
}
