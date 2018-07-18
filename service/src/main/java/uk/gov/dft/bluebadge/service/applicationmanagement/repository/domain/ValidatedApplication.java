package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.io.Serializable;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;

/**
 * Application with helper methods + a marker that validation has happened. The ApplicationConverter
 * expects validation to have occurred and does not recheck for nulls.
 */
public class ValidatedApplication extends Application implements Serializable {

  public boolean isPerson() {
    return PartyTypeCodeField.PERSON.equals(getParty().getTypeCode());
  }
}
