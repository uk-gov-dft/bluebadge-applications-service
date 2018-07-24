package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

/** ReferenceDataResponse */
@Validated
public class ReferenceDataResponse extends CommonResponse {
  @JsonProperty("data")
  @Valid
  private final List<ReferenceData> data = null;

  public List<ReferenceData> getData() {
    return data;
  }
}
