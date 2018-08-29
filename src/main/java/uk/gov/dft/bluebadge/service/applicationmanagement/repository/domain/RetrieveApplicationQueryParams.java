package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RetrieveApplicationQueryParams {

  private UUID uuid;
}
