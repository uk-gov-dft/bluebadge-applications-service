package uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;

@Component
@Slf4j
public class ReferenceDataService {

  private Set<String> authorityKeys;
  private final ReferenceDataApiClient referenceDataApiClient;
  private boolean isLoaded = false;

  @Autowired
  public ReferenceDataService(ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  public boolean isAuthorityCodeValid(String code) {
    if (!isLoaded) init();
    return code != null && authorityKeys.contains(code);
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting.
   */
  private void init() {
    if (!isLoaded) {
      Map<String, Set<String>> groupLists = new HashMap<>();
      Set<String> unexpectedGroups = new HashSet<>();

      // Get list of expected ref data groups
      for (RefDataGroupEnum val : RefDataGroupEnum.values()) {
        groupLists.put(val.getGroupKey(), new HashSet<>());
      }

      List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData("APP");
      for (ReferenceData item : referenceDataList) {
        Set<String> group = groupLists.get(item.getGroupShortCode());
        if (null == group) {
          unexpectedGroups.add(item.getGroupShortCode());
        } else {
          groupLists.get(item.getGroupShortCode()).add(item.getShortCode());
        }
      }

      for (String group : unexpectedGroups) {
        log.warn("Ref data contains group {} that is not used by the application.", group);
      }

      // Store valid authority ids.
      authorityKeys = groupLists.get(RefDataGroupEnum.LOCAL_AUTHORITY.getGroupKey());

      // A bit of validation.
      for (RefDataGroupEnum group : RefDataGroupEnum.values()) {
        validateModelToRefData(
            group.getEnumClass(), groupLists.get(group.getGroupKey()), group.getGroupKey());
      }

      isLoaded = true;
    }
  }

  private int validateModelToRefData(
      Class<? extends Enum<?>> model, Set<String> refValues, String context) {
    int errors = 0;

    if (null == model) {
      log.debug("LoadRefData: Not validating {}", context);
      return 0;
    }

    if (null == refValues) {
      log.error("No reference data loaded for group: {}", context);
      return 1;
    }

    Set<String> enumNames = new HashSet<>();
    for (Enum e : model.getEnumConstants()) {
      enumNames.add(e.name());
    }

    // Is each model value in ref data.
    for (String e : enumNames) {
      if (!refValues.contains(e)) {
        log.error("{}: Enum value {}, not in ref data.", context, e);
        errors++;
      }
    }

    // And the reverse
    for (String e : refValues) {
      if (!enumNames.contains(e)) {
        log.error("{}: Ref data value {}, not in enum.", context, e);
        errors++;
      }
    }

    return errors;
  }
}
