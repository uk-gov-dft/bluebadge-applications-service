package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ValidationKeyEnum.MISSING_ELIGIBILITY_OBJECT;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ValidatedApplication;

@Component
public class ApplicationValidator {

  public ValidatedApplication validateForCreate(Application application) {

    ValidatedApplication validatedApplication = (ValidatedApplication) application;
    checkCoreObjectsPresent(validatedApplication);

    // TODO
    // Person mandatory fields?
    // TODO and for that matter what else is mandatory?
    if (validatedApplication.isPerson()) {
      checkEligibility(validatedApplication);
    }

    return validatedApplication;
  }

  void checkEligibility(ValidatedApplication application) {}

  void checkCoreObjectsPresent(ValidatedApplication application) {
    // Always expect party and contact.
    if (null == application.getParty()) {
      throw new BadRequestException(ValidationKeyEnum.MISSING_PARTY_OBJECT.getFieldErrorInstance());
    }

    if (null == application.getParty().getContact()) {
      throw new BadRequestException(
          ValidationKeyEnum.MISSING_CONTACT_OBJECT.getFieldErrorInstance());
    }

    if (application.isPerson()) {
      if (null == application.getEligibility()) {
        throw new BadRequestException(MISSING_ELIGIBILITY_OBJECT.getFieldErrorInstance());
      }
      if (null == application.getParty().getPerson()) {
        throw new BadRequestException(
            ValidationKeyEnum.MISSING_PERSON_OBJECT.getFieldErrorInstance());
      }
    }

    // Organisation specific stuff
    if (!application.isPerson() && null == application.getParty().getOrganisation()) {
      throw new BadRequestException(ValidationKeyEnum.MISSING_ORG_OBJECT.getFieldErrorInstance());
    }
  }
}
