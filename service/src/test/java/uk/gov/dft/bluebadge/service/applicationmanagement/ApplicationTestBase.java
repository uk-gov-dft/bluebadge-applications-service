package uk.gov.dft.bluebadge.service.applicationmanagement;

import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum.*;

import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.RefDataGroupEnum;
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
    addRefData();
  }

  private ReferenceData getNewRefDataItem(RefDataGroupEnum group, String key, String description) {
    return new ReferenceData()
        .groupShortCode(group.getGroupKey())
        .shortCode(key)
        .description(description);
  }

  private void addRefData() {
    List<ReferenceData> referenceDataList = new ArrayList<>();
    referenceDataList.add(getNewRefDataItem(APP_SOURCE, DefaultVals.APP_CHANNEL_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(DELIVERY_OPTIONS, DefaultVals.DELIVER_OPTION_CODE, null));
    referenceDataList.add(getNewRefDataItem(DELIVER_TO, DefaultVals.DELIVER_TO_CODE, null));
    referenceDataList.add(getNewRefDataItem(ELIGIBILITY, DefaultVals.ELIGIBILITY_CODE, null));
    referenceDataList.add(getNewRefDataItem(PARTY, DefaultVals.PARTY_ORG_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(PARTY, DefaultVals.PARTY_PERSON_CODE, DefaultVals.PARTY_PERSON_DESC));
    referenceDataList.add(
        getNewRefDataItem(GENDER, DefaultVals.GENDER_CODE, DefaultVals.GENDER_DESC));

    when(referenceDataApiClient.retrieveReferenceData()).thenReturn(referenceDataList);
    referenceDataService = new ReferenceDataService(referenceDataApiClient);
    referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, "MALE");
  }
}
