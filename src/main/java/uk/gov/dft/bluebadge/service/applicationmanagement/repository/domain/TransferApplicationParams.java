package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferApplicationParams {
  UUID applicationId;
  String transferToLaShortCode;
  String transferFromLaShortCode;
}
