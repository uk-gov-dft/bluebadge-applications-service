package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifacts;
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
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    // Given party type, party, and contact are ok, then ok to validate.
    assertTrue(applicationValidator.hasParty(errors));
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_missing_party() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    errors.rejectValue(FieldKeys.KEY_PARTY, "");

    // Given the check failed ensure main validate method does nothing
    // And particularly has no null pointers.
    applicationValidator.validate(app, errors);
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_invalid_party_type() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    // Given party type failed bean validation can't validate further
    errors.rejectValue(FieldKeys.KEY_PARTY_TYPE, "Invalid");
    app.getParty().setTypeCode(PartyTypeCodeField.PERSON);
    assertFalse(applicationValidator.hasParty(errors));
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void validateLocalAuthority() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

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
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build());

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
    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build());

    // When person valid, no errors added.
    applicationValidator.validatePerson(app, errors);
    assertEquals(0, errors.getErrorCount());

    // When Person, organisation must be null
    addOrganisation(app);
    reset();
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORGANISATION));

    // Reset
    app.getParty().setOrganisation(null);

    // When Person, eligibility required
    app.setEligibility(null);
    reset();
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    setEligibilityPip(app);

    // When Person, person required
    app.getParty().setPerson(null);
    reset();
    applicationValidator.validatePerson(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));
  }

  @Test
  public void validateOrganisation() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

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
    reset();
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));

    // Reset
    app.getParty().setPerson(null);

    // Cannot have eligibility object
    setEligibilityPip(app);
    reset();
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    app.setEligibility(null);

    // Cannot have artifacts
    app.setArtifacts(new Artifacts());
    reset();
    applicationValidator.validateOrganisation(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ARTIFACTS));
  }

  @Test
  public void validateCharity() {
    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());

    // Cannot have charity number if not charity
    app.getParty().getOrganisation().setIsCharity(false);
    app.getParty().getOrganisation().setCharityNumber(ValidValues.CHARITY_NO);
    reset();
    applicationValidator.validateCharity(app, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORG_CHARITY_NO));
  }

  @Test
  public void validate() {
    // Couple of runs through the whole validate to check safe for null pointers.
    reset(getApplicationBuilder().addBaseApplication().build());
    applicationValidator.validate(app, errors);

    reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build());
    applicationValidator.validate(app, errors);

    reset(getApplicationBuilder().addBaseApplication().setOrganisation().build());
    applicationValidator.validate(app, errors);
  }
}
