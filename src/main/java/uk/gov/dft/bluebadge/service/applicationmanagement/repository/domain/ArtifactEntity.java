package uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Alias("ArtifactEntity")
@Data
@Builder
public class ArtifactEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @NonNull private UUID applicationId;
  @NonNull private String type;
  @NonNull private String link;
}
