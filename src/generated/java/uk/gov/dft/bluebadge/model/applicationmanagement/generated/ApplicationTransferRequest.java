package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ApplicationTransferRequest {
  @NotEmpty private String transferToLaShortCode;
}
