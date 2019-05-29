package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

public class ApplicationValidatorTest extends ApplicationFixture {

  private ApplicationValidator applicationValidator;

  @Mock private EligibilityValidator eligibilityValidator;

  @Mock protected ReferenceDataApiClient referenceDataApiClient;

  public ApplicationValidatorTest() {
    MockitoAnnotations.initMocks(this);
    ReferenceDataService referenceDataService = initValidRefData(referenceDataApiClient);
    applicationValidator = new ApplicationValidator(referenceDataService, eligibilityValidator);
  }

  @Test
  public void checkRequiredObjectsToContinueValid() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // Given party type, party, and contact are ok, then ok to validate.
    assertTrue(applicationValidator.hasParty(errors));
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_missing_party() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);
    errors.rejectValue(FieldKeys.KEY_PARTY, "");

    // Given the check failed ensure main validate method does nothing
    // And particularly has no null pointers.
    applicationValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_invalid_party_type() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // Given party type failed bean validation can't validate further
    errors.rejectValue(FieldKeys.KEY_PARTY_TYPE, "Invalid");
    app.getParty().setTypeCode(PartyTypeCodeField.PERSON);
    assertFalse(applicationValidator.hasParty(errors));
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void validateLocalAuthority() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // Given KEY_LA valid, no errors.
    applicationValidator.validateLocalAuthority(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Given KEY_LA ivalid, then error
    app.setLocalAuthorityCode("ABC");
    applicationValidator.validateLocalAuthority(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_LA));
  }

  @Test
  public void validateDob() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // When dob valid, no errors added.
    applicationValidator.validateDob(app, errors);
    assertEquals(0, errors.getErrorCount());

    // When KEY_PERSON_DOB in future get error
    app.getParty().getPerson().setDob(LocalDate.now().plus(Period.ofYears(1)));
    applicationValidator.validateDob(app, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON_DOB));
  }

  @Test
  public void validatePerson() {
    Application app =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // When person valid, no errors added.
    applicationValidator.validatePerson(app, errors);
    assertEquals(0, errors.getErrorCount());

    // When Person, organisation must be null
    addOrganisation(app);
    errors = getNewBindingResult(app);
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORGANISATION));

    // Reset
    app.getParty().setOrganisation(null);

    // When Person, eligibility required
    app.setEligibility(null);
    errors = getNewBindingResult(app);
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    setEligibilityPip(app);

    // When Person, person required
    app.getParty().setPerson(null);
    errors = getNewBindingResult(app);
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));
  }

  @Test
  public void validateOrganisation() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // Valid
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have org object
    app.getParty().setOrganisation(null);
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORGANISATION));

    // Reset
    addOrganisation(app);

    // Cannot have person object
    addPerson(app);
    errors = getNewBindingResult(app);
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));

    // Reset
    app.getParty().setPerson(null);

    // Cannot have eligibility object
    setEligibilityPip(app);
    errors = getNewBindingResult(app);
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    app.setEligibility(null);

    // Cannot have artifacts
    app.setArtifacts(new ArrayList<>());
    errors = getNewBindingResult(app);
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ARTIFACTS));
  }

  @Test
  public void validateCharity() {
    Application app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    BeanPropertyBindingResult errors = getNewBindingResult(app);

    // Cannot have charity number if not charity
    app.getParty().getOrganisation().setIsCharity(false);
    app.getParty().getOrganisation().setCharityNumber(ValidValues.CHARITY_NO);
    applicationValidator.validateCharity(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORG_CHARITY_NO));
  }

  @Test
  public void validate() {
    // Couple of runs through the whole validate to check safe for null pointers.
    Application app = getApplicationBuilder().addBaseApplication().build();
    applicationValidator.validate(app, getNewBindingResult(app));

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    applicationValidator.validate(app, getNewBindingResult(app));

    app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    applicationValidator.validate(app, getNewBindingResult(app));
  }
}
