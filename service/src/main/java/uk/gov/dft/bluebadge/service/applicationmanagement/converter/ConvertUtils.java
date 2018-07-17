package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.apache.commons.lang3.StringUtils;

public class ConvertUtils {

  private ConvertUtils() {}

  public static String formatPostcodeForEntity(String postcode) {
    if (null == postcode) return null;

    return StringUtils.removeAll(postcode.toUpperCase(), " ");
  }
}
