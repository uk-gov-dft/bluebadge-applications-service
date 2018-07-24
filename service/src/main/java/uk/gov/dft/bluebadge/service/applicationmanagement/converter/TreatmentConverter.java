package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Treatment;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;

@Component
class TreatmentConverter implements ApplicationToEntityCollection<TreatmentEntity, Treatment> {

  @Override
  public TreatmentEntity mapToEntity(Treatment model, UUID applicationId) {
    return TreatmentEntity.builder()
        .applicationId(applicationId)
        .description(model.getDescription())
        .time(model.getTime())
        .build();
  }
}
