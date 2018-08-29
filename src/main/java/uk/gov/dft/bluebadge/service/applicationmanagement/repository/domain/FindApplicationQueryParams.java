package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;

@Getter
@Setter
@Builder
@Alias("FindApplicationQueryParams")
public class FindApplicationQueryParams {
  String name;
  String postcode;
  ApplicationTypeCodeField applicationTypeCode;
  Instant submissionFrom;
  Instant submissionTo;
  @NotNull String authorityCode;
}
