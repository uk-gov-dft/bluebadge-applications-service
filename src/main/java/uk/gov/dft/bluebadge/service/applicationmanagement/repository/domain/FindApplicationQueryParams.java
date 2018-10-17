package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;

@Data
@Builder
@Alias("FindApplicationQueryParams")
public class FindApplicationQueryParams {
  private String name;
  private String postcode;
  private ApplicationTypeCodeField applicationTypeCode;
  private Instant submissionFrom;
  private Instant submissionTo;
  private @NotNull String authorityCode;
  private boolean deleted;
}
