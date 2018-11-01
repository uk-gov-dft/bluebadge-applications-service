package uk.gov.dft.bluebadge.service.applicationmanagement.service.audit;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.logging.LogEventBuilder;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;

@Component
public class ApplicationAuditLogger {
  @Getter
  enum AuditEventFields {
    CREATE(
        LogEventBuilder.AuditEvent.APPLICATION_CREATED,
        "eligibility.typeCode",
        "localAuthorityCode",
        "submissionDate");

    private final LogEventBuilder.AuditEvent event;
    private final String[] fields;

    AuditEventFields(LogEventBuilder.AuditEvent event, String... fields) {
      this.event = event;
      this.fields = fields;
    }
  }

  public void logCreateAuditEvent(Application application, Logger log) {
    LogEventBuilder.builder()
        .forObject(application)
        .forEvent(AuditEventFields.CREATE.getEvent())
        .withLogger(log)
        .withFields(AuditEventFields.CREATE.getFields())
        .log();
  }
}
