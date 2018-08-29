package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

import java.util.UUID;

@Component
public class HealthcareProfessionalConverter
    implements ApplicationToEntityCollection<HealthcareProfessionalEntity, HealthcareProfessional> {

  @Override
  public HealthcareProfessionalEntity mapToEntity(
      HealthcareProfessional healthcareProfessional, UUID applicationId) {
    return HealthcareProfessionalEntity.builder()
        .profLocation(healthcareProfessional.getLocation())
        .profName(healthcareProfessional.getName())
        .applicationId(applicationId)
        .build();
  }

  @Override
  public HealthcareProfessional mapToModel(HealthcareProfessionalEntity entity) {
    // TODO
    return null;
  }
}
