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
    CREATE_FIELDS(
        "eligibility.typeCode",
        "localAuthorityCode",
        "submissionDate");

    private final String[] fields;

    AuditEventFields(String... fields) {
      this.fields = fields;
    }
  }

  public void logCreateAuditEvent(Application application, Logger log) {
    LogEventBuilder.builder()
        .forObject(application)
        .forEvent(LogEventBuilder.AuditEvent.APPLICATION_CREATED)
        .withLogger(log)
        .withFields(AuditEventFields.CREATE_FIELDS.getFields())
        .log();
  }
}
