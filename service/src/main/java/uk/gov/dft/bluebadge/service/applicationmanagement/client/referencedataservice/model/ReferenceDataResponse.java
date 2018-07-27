package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@EqualsAndHashCode(callSuper = true)
public class ReferenceDataResponse extends CommonResponse {

  @JsonProperty("data")
  private final List<ReferenceData> data = null;

  public List<ReferenceData> getData() {
    return data;
  }
}
