package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.Matchers.collection;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.Matchers.enumValues;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchersTest {

  @Test
  public void testEnums() {

    assertFalse(enumValues(WALKD, CHILDVEHIC).contains(null));
    assertTrue(enumValues(WALKD, CHILDVEHIC).doesNotContain(null));

    assertTrue(enumValues(WALKD, CHILDVEHIC).contains(CHILDVEHIC));
    assertFalse(enumValues(WALKD, CHILDVEHIC).contains(ARMS));

    assertFalse(enumValues(WALKD, CHILDVEHIC).doesNotContain(CHILDVEHIC));
    assertTrue(enumValues(WALKD, CHILDVEHIC).doesNotContain(ARMS));
  }

  @Test
  public void testCollections() {

    assertTrue(collection(null).isNullOrEmpty());
    assertFalse(collection(null).isNotEmpty());

    assertTrue(collection(new ArrayList<String>()).isNullOrEmpty());
    assertFalse(collection(new ArrayList<String>()).isNotEmpty());

    Set<String> strings = new HashSet<>();
    strings.add("a");

    assertFalse(collection(strings).isNullOrEmpty());
    assertTrue(collection(strings).isNotEmpty());
  }

}
