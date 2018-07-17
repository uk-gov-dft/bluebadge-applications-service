package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ApplicationToEntityCollection<E, M> {
  abstract E mapToEntity(M model, UUID applicationId);

  public List<E> convertToEntityList(List<M> modelList, UUID applicationId) {
    if (null == modelList || modelList.isEmpty()) return null;
    List<E> entities = new ArrayList<>();
    for (M modelItem : modelList) {
      entities.add(mapToEntity(modelItem, applicationId));
    }
    return entities;
  }
}
