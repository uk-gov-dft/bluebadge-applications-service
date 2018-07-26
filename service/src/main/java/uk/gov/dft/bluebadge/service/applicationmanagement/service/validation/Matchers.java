package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import java.util.Collection;

public class Matchers {

  static class CollectionMatchers{
    private Collection<?> collection;

    public CollectionMatchers(Collection<?> collection) {
      this.collection = collection;
    }

    public boolean isNullOrEmpty(){
      return null == collection || collection.isEmpty();
    }

    public boolean isNotEmpty(){
      return !isNullOrEmpty();
    }
  }

  static class EnumListMatchers {

    private Enum<?>[] list;

    public EnumListMatchers(Enum<?>... list) {
      this.list = list;
    }

    public boolean contains(Enum<?> value) {
      for (Enum<?> item : list) {
        if (item.equals(value)) {
          return true;
        }
      }
      return false;
    }

    public boolean doesNotContain(Enum<?> value) {
      return !contains(value);
    }
  }

  public static EnumListMatchers enumValues(Enum<?>... list) {
    return new EnumListMatchers(list);
  }

  public static CollectionMatchers collection(Collection<?> collection){
    return new CollectionMatchers(collection);
  }
}
