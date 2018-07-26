package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifacts;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

public class ApplicationValidatorTest extends ApplicationFixture {

  ApplicationValidator applicationValidator;
  @Mock
  EligibilityValidator eligibilityValidator;

  public ApplicationValidatorTest() {
    super();
    applicationValidator = new ApplicationValidator(referenceDataService, eligibilityValidator);
  }

  @Test
  public void checkRequiredObjectsToContinueValid() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given party type, party, and contact are ok, then ok to validate.
    assertTrue(applicationValidator.hasParty(application, errors));
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_missing_party() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    errors.rejectValue(FieldKeys.KEY_PARTY, "");

    // Given the check failed ensure main validate method does nothing
    // And particularly has no null pointers.
    applicationValidator.validate(application, errors);
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void checkRequiredObjectsExistToContinueValidation_invalid_party_type() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given party type failed bean validation can't validate further
    errors.rejectValue(FieldKeys.KEY_PARTY_TYPE, "Invalid");
    application.getParty().setTypeCode(PartyTypeCodeField.PERSON);
    assertFalse(applicationValidator.hasParty(application, errors));
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void validateLocalAuthority() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Given KEY_LA valid, no errors.
    applicationValidator.validateLocalAuthority(application, errors);
    assertEquals(0, errors.getErrorCount());

    // Given KEY_LA ivalid, then error
    application.setLocalAuthorityCode("ABC");
    applicationValidator.validateLocalAuthority(application, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_LA));
  }

  @Test
  public void validatePerson() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityPip().build();
    resetErrors(application);

    // When person valid, no errors added.
    applicationValidator.validatePerson(application, errors);
    assertEquals(0, errors.getErrorCount());

    // When KEY_PERSON_DOB in future get error
    application.getParty().getPerson().setDob(LocalDate.now().plus(Period.ofYears(1)));
    applicationValidator.validatePerson(application, errors);
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON_DOB));

    // Reset
    application.getParty().getPerson().setDob(ValidValues.DOB);

    // When Person, organisation must be null
    addOrganisation(application);
    resetErrors(application);
    applicationValidator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORGANISATION));

    // Reset
    application.getParty().setOrganisation(null);

    // When Person, eligibility required
    application.setEligibility(null);
    resetErrors(application);
    applicationValidator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    setEligibilityPip(application);

    // When Person, person required
    application.getParty().setPerson(null);
    resetErrors(application);
    applicationValidator.validatePerson(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));
  }

  @Test
  public void validateOrganisation() {
    Application application =
        getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(application);

    // Valid
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(0, errors.getErrorCount());

    // Must have org object
    application.getParty().setOrganisation(null);
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORGANISATION));

    // Reset
    addOrganisation(application);

    // Cannot have person object
    addPerson(application);
    resetErrors(application);
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_PERSON));

    // Reset
    application.getParty().setPerson(null);

    // Cannot have eligibility object
    setEligibilityPip(application);
    resetErrors(application);
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELIGIBILITY));

    // Reset
    application.setEligibility(null);

    // Cannot have charity number if not charity
    application.getParty().getOrganisation().setIsCharity(false);
    application.getParty().getOrganisation().setCharityNumber(ValidValues.CHARITY_NO);
    resetErrors(application);
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ORG_CHARITY_NO));

    // Reset
    application.getParty().getOrganisation().setIsCharity(true);

    // Cannot have artifacts
    application.setArtifacts(new Artifacts());
    resetErrors(application);
    applicationValidator.validateOrganisation(application, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ARTIFACTS));
  }

  @Test
  public void validate() {
    // Couple of runs through the whole validate to check safe for null pointers.
    Application app = getApplicationBuilder().addBaseApplication().build();
    resetErrors(app);
    applicationValidator.validate(app, errors);

    app = getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().build();
    resetErrors(app);
    applicationValidator.validate(app, errors);

    app = getApplicationBuilder().addBaseApplication().setOrganisation().build();
    resetErrors(app);
    applicationValidator.validate(app, errors);
  }
}
