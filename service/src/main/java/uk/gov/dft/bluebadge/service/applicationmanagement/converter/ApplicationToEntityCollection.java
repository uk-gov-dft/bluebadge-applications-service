package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

interface ApplicationToEntityCollection<E, M> {
  E mapToEntity(M model, UUID applicationId);

  default List<E> convertToEntityList(List<M> modelList, UUID applicationId) {
    List<E> entities = new ArrayList<>();
    if (null != modelList && !modelList.isEmpty()) {
      for (M modelItem : modelList) {
        entities.add(mapToEntity(modelItem, applicationId));
      }
    }
    return entities;
  }
}
