package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingAid;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;

@Component
public class WalkingAidConverter
    implements ApplicationToEntityCollection<WalkingAidEntity, WalkingAid> {
  @Override
  public WalkingAidEntity mapToEntity(WalkingAid model, UUID applicationId) {
    return WalkingAidEntity.builder()
        .applicationId(applicationId)
        .description(model.getDescription())
        .howProvidedCode(model.getHowProvidedCode().toString())
        .usage(model.getUsage())
        .build();
  }
}
