package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LocalAuthorityRefData extends ReferenceData {

  @JsonProperty("metaData")
  private LocalAuthorityMetaData localAuthorityMetaData = null;

  @JsonIgnore
  public String getContactUrl() {
    return getLocalAuthorityMetaData().map(LocalAuthorityMetaData::getContactUrl).orElse(null);
  }

  public Optional<LocalAuthorityMetaData> getLocalAuthorityMetaData() {
    return Optional.ofNullable(localAuthorityMetaData);
  }

  @Data
  public static class LocalAuthorityMetaData implements Serializable {
    private String issuingAuthorityShortCode;
    private String issuingAuthorityName;
    private String contactUrl;
    private String differentServiceSignpostUrl;
  }
}
