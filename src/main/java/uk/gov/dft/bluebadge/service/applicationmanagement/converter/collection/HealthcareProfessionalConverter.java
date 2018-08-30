package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HealthcareProfessional;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

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
    HealthcareProfessional model = new HealthcareProfessional();
    model.setLocation(entity.getProfLocation());
    model.setName(entity.getProfName());
    return model;
  }
}
