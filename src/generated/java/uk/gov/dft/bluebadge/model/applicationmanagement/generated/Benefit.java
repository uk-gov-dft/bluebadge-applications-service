package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Benefit {
  private Boolean isIndefinite;
  private LocalDate expiryDate;
}
