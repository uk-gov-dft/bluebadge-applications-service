package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationTestBase;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum;

public class ReferenceDataServiceTest extends ApplicationTestBase {

  @Test
  public void groupContainsKey() {
    Assert.assertTrue(
        referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
    Assert.assertFalse(referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, "VOLVO"));
  }

  @Test
  public void getDescription() {
    assertEquals(
        DefaultVals.GENDER_DESC,
        referenceDataService.getDescription(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
  }
}
