package uk.gov.dft.bluebadge.service.applicationmanagement.service.audit;

import lombok.Getter;
import uk.gov.dft.bluebadge.common.logging.LogEventBuilder.AuditEvent;

@Getter
public enum AuditEventFields {
  CREATE(
      AuditEvent.APPLICATION_CREATED,
      "eligibility.typeCode",
      "localAuthorityCode",
      "submissionDate");

  private final AuditEvent event;
  private final String[] fields;

  AuditEventFields(AuditEvent event, String... fields) {
    this.event = event;
    this.fields = fields;
  }
}
