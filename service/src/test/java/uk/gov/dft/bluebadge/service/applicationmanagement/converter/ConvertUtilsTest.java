package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.junit.Assert;
import org.junit.Test;

public class ConvertUtilsTest {

  @Test
  public void formatPostcodeForEntity() {
    // Given postcode has lowercase chars and a space
    String postcode = "wv16 4aw";
    // When converted
    postcode = ConvertUtils.formatPostcodeForEntity(postcode);
    // Then space removed and uppercase.
    Assert.assertEquals("WV164AW", postcode);
  }

  @Test
  public void formatPostcodeForEntity_null_safe() {
    // Given postcode null
    // When converted all ok
    ConvertUtils.formatPostcodeForEntity(null);
  }
}
