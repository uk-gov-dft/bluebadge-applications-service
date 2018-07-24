package uk.gov.dft.bluebadge.service.applicationmanagement.client.referencedataservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferenceData {
  private String shortCode;
  private String description;
  private String groupShortCode;
  private String groupDescription;
  private String subgroupShortCode;
  private String subgroupDescription;
  private Integer displayOrder;
}
