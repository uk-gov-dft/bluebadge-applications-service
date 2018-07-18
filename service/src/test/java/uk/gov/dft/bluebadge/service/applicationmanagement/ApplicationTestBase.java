package uk.gov.dft.bluebadge.service.applicationmanagement;

import static uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

public class ApplicationTestBase {

  public class DefaultVals {
    static final String ELIGIBILITY_CODE = "PIP";
    static final String DELIVER_TO_CODE = "HOME";
    static final String DELIVER_OPTION_CODE = "FAST";
    static final String APP_CHANNEL_CODE = "ONLINE";
    public static final String GENDER_CODE = "MALE";
    public static final String GENDER_DESC = "Male";
    static final String PARTY_PERSON_CODE = "PERSON";
    public static final String PARTY_PERSON_DESC = "Person";
    static final String PARTY_ORG_CODE = "ORG";
  }

  protected ReferenceDataService referenceDataService;

  @Mock private ReferenceDataApiClient referenceDataApiClient;

  public ApplicationTestBase() {
    MockitoAnnotations.initMocks(this);
  }
}
