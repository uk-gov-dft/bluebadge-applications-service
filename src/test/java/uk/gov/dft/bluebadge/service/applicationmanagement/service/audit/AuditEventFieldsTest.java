package uk.gov.dft.bluebadge.service.applicationmanagement.service.audit;

import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditEventFieldsTest extends ApplicationFixture {

  @Test
  public void auditEventFieldsExistInApplication() {
    // Given a fully populated application
    Application application =
        getApplicationBuilder()
            .addBaseApplication()
            .setEligibilityWalking()
            .setPerson()
            .setOrganisation()
            .build();
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext context = new StandardEvaluationContext(application);

    // Then for each audit event all the configured fields exist in the application
    for (AuditEventFields event : AuditEventFields.values()) {
      for (String field : event.getFields()) {
        assertThat(parser.parseExpression(field).getValue(context)).isNotNull();
      }
    }
  }
}
