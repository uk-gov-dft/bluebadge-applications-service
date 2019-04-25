package uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class ApplicationToEntityCollectionTest {

  private String ENTITY_VALUE = "Entity Value";
  private String MODEL_VALUE = "Model Value";

  class CollectionTestClass implements ApplicationToEntityCollection<String, String> {

    @Override
    public String mapToEntity(String model, UUID applicationId) {
      return ENTITY_VALUE;
    }

    @Override
    public String mapToModel(String entity) {
      return MODEL_VALUE;
    }
  }

  private CollectionTestClass test = new CollectionTestClass();

  @Test
  public void convertToEntityList() {
    List<String> modelList = Lists.newArrayList("1234", "gg");
    List<String> entityList = test.convertToEntityList(modelList, null);

    assertEquals(ENTITY_VALUE, entityList.get(0));
    assertEquals(ENTITY_VALUE, entityList.get(1));
  }

  @Test
  public void convertToModelList() {
    List<String> entityList = Lists.newArrayList("1234", "gg");
    List<String> modelList = test.convertToModelList(entityList);

    assertEquals(MODEL_VALUE, modelList.get(0));
    assertEquals(MODEL_VALUE, modelList.get(1));
  }
}
