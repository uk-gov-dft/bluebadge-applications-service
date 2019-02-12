package uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model.ReferenceData;

@Component
@Slf4j
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReferenceDataService {

  private final HashMap<String, LocalAuthorityRefData> authorities = new HashMap<>();
  private final Map<String, ReferenceData> eligibilities = new HashMap<>();
  private final ReferenceDataApiClient referenceDataApiClient;
  private AtomicBoolean isLoaded = new AtomicBoolean(false);

  @Autowired
  public ReferenceDataService(@Validated ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  public boolean isAuthorityCodeValid(String code) {
    init();
    return code != null && authorities.containsKey(code);
  }

  public LocalAuthorityRefData getLocalAuthority(String code) {
    init();
    Assert.notNull(code, "Local authority code is null");
    return authorities.get(code);
  }

  public String getEligibilityDescription(String code) {
    init();
    Assert.notNull(code, "Eligibility code is null");
    ReferenceData referenceData = eligibilities.get(code);
    return referenceData == null ? null : referenceData.getDescription();
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting. Not done on startup. Else would be start order
   * dependency between services.
   */
  private void init() {
    if (!isLoaded.getAndSet(true)) {

      log.info("Loading reference data.");
      List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData("APP");
      if (!referenceDataList.isEmpty()) {
        // Store valid authority ids.
        for (ReferenceData item : referenceDataList) {
          if (RefDataGroupEnum.LOCAL_AUTHORITY.getGroupKey().equals(item.getGroupShortCode())) {
            authorities.put(item.getShortCode(), (LocalAuthorityRefData) item);
          } else if (RefDataGroupEnum.ELIGIBILITY.getGroupKey().equals(item.getGroupShortCode())) {
            eligibilities.put(item.getShortCode(), item);
          }
        }

        if (log.isDebugEnabled()) {
          validate(referenceDataList);
        }
        log.info("Reference data loaded.");
      }
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
      log.warn("No reference data loaded for group: {}", context);
      return;
    }

    Set<String> enumNames = new HashSet<>();
    for (Enum e : model.getEnumConstants()) {
      enumNames.add(e.name());
    }

    // Is each model value in ref data.
    for (String e : enumNames) {
      if (!refValues.contains(e)) {
        log.warn("{}: Enum value {}, not in ref data.", context, e);
      }
    }

    // And the reverse
    for (String e : refValues) {
      if (!enumNames.contains(e)) {
        log.warn("{}: Ref data value {}, not in enum.", context, e);
      }
    }
  }
}
