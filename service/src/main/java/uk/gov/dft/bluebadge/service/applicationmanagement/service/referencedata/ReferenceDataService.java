package uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class ReferenceDataService {

  private Set<String> authorityKeys = new HashSet<>();
  private final ReferenceDataApiClient referenceDataApiClient;
  private boolean isLoaded = false;

  @Autowired
  public ReferenceDataService(@Validated ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  public boolean isAuthorityCodeValid(String code) {
    if (!isLoaded) init();
    return code != null && authorityKeys.contains(code);
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting. Not done on startup. Else would be start order
   * dependency between services.
   */
  private void init() {
    if (!isLoaded) {

      log.info("Loading reference data.");
      List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData("APP");

      // Store valid authority ids.
      for (ReferenceData item : referenceDataList) {
        if (RefDataGroupEnum.LOCAL_AUTHORITY.getGroupKey().equals(item.getGroupShortCode())) {
          authorityKeys.add(item.getShortCode());
        }
      }

      if (log.isDebugEnabled()) {
        validate(referenceDataList);
      }
      log.info("Reference data loaded.");
      isLoaded = true;
    }
  }

  /**
   * Dev/debug method to log discrepancies between DB ref data and jave enums. Logs out when enum
   * has values not in DB or vice versa.
   *
   * @param referenceDataList List of reference data for this service.
   */
  private void validate(List<ReferenceData> referenceDataList) {
    Map<String, Set<String>> groupLists = new HashMap<>();
    Set<String> unexpectedGroups = new HashSet<>();

    // Get list of expected ref data groups
    for (RefDataGroupEnum val : RefDataGroupEnum.values()) {
      groupLists.put(val.getGroupKey(), new HashSet<>());
    }

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
    // A bit of validation.
    for (RefDataGroupEnum group : RefDataGroupEnum.values()) {
      validateModelToRefData(
          group.getEnumClass(), groupLists.get(group.getGroupKey()), group.getGroupKey());
    }
  }

  private void validateModelToRefData(
      Class<? extends Enum<?>> model, Set<String> refValues, String context) {

    if (null == model) {
      log.debug("LoadRefData: Not validating {}", context);
      return;
    }

    if (null == refValues) {
      log.error("No reference data loaded for group: {}", context);
      return;
    }

    Set<String> enumNames = new HashSet<>();
    for (Enum e : model.getEnumConstants()) {
      enumNames.add(e.name());
    }

    // Is each model value in ref data.
    for (String e : enumNames) {
      if (!refValues.contains(e)) {
        log.error("{}: Enum value {}, not in ref data.", context, e);
      }
    }

    // And the reverse
    for (String e : refValues) {
      if (!enumNames.contains(e)) {
        log.error("{}: Ref data value {}, not in enum.", context, e);
      }
    }
  }
}
