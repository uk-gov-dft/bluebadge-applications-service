package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class Benefit {
  private Boolean isIndefinite;
  private LocalDate expiryDate;
}
