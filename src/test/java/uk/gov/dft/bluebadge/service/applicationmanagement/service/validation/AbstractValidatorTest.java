package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class AbstractValidatorTest extends ApplicationFixture {

  @Test
  public void rejectIfExistsTest() {
    // Given object person does not exist and organisation does
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    // When reject person if exists
    rejectIfExists(errors, FieldKeys.KEY_PERSON, "");

    // Then not rejected
    assertEquals(0, errors.getErrorCount());

    // When reject org if exists
    rejectIfExists(errors, FieldKeys.KEY_ORGANISATION, "");

    // Then rejected
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void rejectIfEmptyOrWhitespaceTest() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().build());

    // When not empty, no error
    rejectIfEmptyOrWhitespace(errors, FieldKeys.KEY_PERSON_DOB, "");

    // When empty error
    app.getParty().getPerson().setBadgeHolderName("");
    reset();
    rejectIfEmptyOrWhitespace(errors, "party.person.badgeHolderName", "");
    assertEquals(1, errors.getFieldErrorCount("party.person.badgeHolderName"));

    // When null error
    app.getParty().getPerson().setBadgeHolderName(null);
    reset();
    rejectIfEmptyOrWhitespace(errors, "party.person.badgeHolderName", "");
    assertEquals(1, errors.getFieldErrorCount("party.person.badgeHolderName"));
  }

  @Test
  public void hasNoFieldErrors_Test() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().build());

    assertTrue(hasNoFieldErrors(errors, FieldKeys.KEY_PERSON));

    errors.rejectValue(FieldKeys.KEY_PERSON, "ABC");
    assertFalse(hasNoFieldErrors(errors, FieldKeys.KEY_PERSON));
  }

  @Test
  public void hasFieldErrors_Test() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().build());

    assertFalse(hasFieldErrors(errors, FieldKeys.KEY_PERSON));

    errors.rejectValue(FieldKeys.KEY_PERSON, "ABC");
    assertTrue(hasFieldErrors(errors, FieldKeys.KEY_PERSON));
  }

  @Test
  public void exists_Test() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().build());

    assertTrue(exists(errors, FieldKeys.KEY_PERSON));
    assertFalse(exists(errors, FieldKeys.KEY_ORGANISATION));
  }

  @Test
  public void notExists_Test() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    assertTrue(notExists(errors, FieldKeys.KEY_PERSON));
    assertFalse(notExists(errors, FieldKeys.KEY_ORGANISATION));
  }

  @Test
  public void hasText_Test() {
    reset(getApplicationBuilder().addBaseApplication().setPerson().build());
    assertTrue(hasText(errors, "party.person.badgeHolderName"));

    app.getParty().getPerson().setBadgeHolderName("");
    reset();
    assertFalse(hasText(errors, "party.person.badgeHolderName"));

    app.getParty().getPerson().setBadgeHolderName(null);
    reset();
    assertFalse(hasText(errors, "party.person.badgeHolderName"));
  }
}
