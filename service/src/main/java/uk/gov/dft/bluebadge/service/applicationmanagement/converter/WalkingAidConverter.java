package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.UUID;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingAid;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;

public class WalkingAidConverter
    extends ApplicationToEntityCollection<WalkingAidEntity, WalkingAid> {
  @Override
  WalkingAidEntity mapToEntity(WalkingAid model, UUID applicationId) {
    return WalkingAidEntity.builder()
        .applicationId(applicationId)
        .description(model.getDescription())
        .howProvidedCode(model.getHowProvidedCode())
        .usage(model.getUsage())
        .build();
  }
}
