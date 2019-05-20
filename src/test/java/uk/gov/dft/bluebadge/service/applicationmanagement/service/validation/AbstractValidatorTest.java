package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class AbstractValidatorTest extends ApplicationFixture {

  @Test
  public void rejectIfExistsTest() {
    // Given object person does not exist and organisation does
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // When reject person if exists
    rejectIfExists(app, errors, FieldKeys.KEY_PERSON, "");

    // Then not rejected
    assertEquals(0, errors.getErrorCount());

    // When reject org if exists
    rejectIfExists(app, errors, FieldKeys.KEY_ORGANISATION, "");

    // Then rejected
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void rejectIfEmptyOrWhitespaceTest() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    // When not empty, no error
    rejectIfEmptyOrWhitespace(errors, FieldKeys.KEY_PERSON_DOB, "");

    // When empty error
    app.getParty().getPerson().setBadgeHolderName("");
    errors = getNewBindingResult(app);
    rejectIfEmptyOrWhitespace(errors, "party.person.badgeHolderName", "");
    assertEquals(1, errors.getFieldErrorCount("party.person.badgeHolderName"));

    // When null error
    app.getParty().getPerson().setBadgeHolderName(null);
    errors = getNewBindingResult(app);
    rejectIfEmptyOrWhitespace(errors, "party.person.badgeHolderName", "");
    assertEquals(1, errors.getFieldErrorCount("party.person.badgeHolderName"));
  }

  @Test
  public void hasNoFieldErrors_Test() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    assertTrue(hasNoFieldErrors(errors, FieldKeys.KEY_PERSON));

    errors.rejectValue(FieldKeys.KEY_PERSON, "ABC");
    assertFalse(hasNoFieldErrors(errors, FieldKeys.KEY_PERSON));
  }

  @Test
  public void hasFieldErrors_Test() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    assertFalse(hasFieldErrors(errors, FieldKeys.KEY_PERSON));

    errors.rejectValue(FieldKeys.KEY_PERSON, "ABC");
    assertTrue(hasFieldErrors(errors, FieldKeys.KEY_PERSON));
  }

  @Test
  public void exists_Test() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().build();

    assertTrue(exists(app, FieldKeys.KEY_PERSON));
    assertFalse(exists(app, FieldKeys.KEY_ORGANISATION));
  }

  @Test
  public void notExists_Test() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();

    assertTrue(notExists(app, FieldKeys.KEY_PERSON));
    assertFalse(notExists(app, FieldKeys.KEY_ORGANISATION));
  }

  @Test
  public void hasText_Test() {
    Application app = getApplicationBuilder().addBaseApplication().setPerson().build();
    assertTrue(hasText(app, "party.person.badgeHolderName"));

    app.getParty().getPerson().setBadgeHolderName("");
    assertFalse(hasText(app, "party.person.badgeHolderName"));

    app.getParty().getPerson().setBadgeHolderName(null);
    assertFalse(hasText(app, "party.person.badgeHolderName"));
  }
}
