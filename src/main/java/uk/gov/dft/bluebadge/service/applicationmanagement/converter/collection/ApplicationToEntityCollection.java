package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

interface ApplicationToEntityCollection<E, M> {
  E mapToEntity(M model, UUID applicationId);

  M mapToModel(E entity);

  default List<E> convertToEntityList(List<M> modelList, UUID applicationId) {
    List<E> entities = new ArrayList<>();
    if (null != modelList && !modelList.isEmpty()) {
      for (M modelItem : modelList) {
        entities.add(mapToEntity(modelItem, applicationId));
      }
    }
    return entities;
  }

  default List<M> convertToModelList(List<E> entityList) {

    if (null != entityList && !entityList.isEmpty()) {
      List<M> models = new ArrayList<>();
      for (E entity : entityList) {
        models.add(mapToModel(entity));
      }
      return models;
    }
    return null;
  }
}
