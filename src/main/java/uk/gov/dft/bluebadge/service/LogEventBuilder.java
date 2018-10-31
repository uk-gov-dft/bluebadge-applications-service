package uk.gov.dft.bluebadge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

public class LogEventBuilder {

  public enum AuditEvent {
    APPLICATION_CREATE
  }

  private Map<String, Object> data = new HashMap<>();
  private ExpressionParser parser = new SpelExpressionParser();
  private StandardEvaluationContext context;
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private AuditEvent event;
  private Logger log;

  public LogEventBuilder() {

  }

  public LogEventBuilder forEvent(AuditEvent event){
    this.event = event;
    return this;
  }

  public LogEventBuilder withLogger(Logger log){
    this.log = log;
    return this;
  }

  public LogEventBuilder forObject(Object auditDataSource){
    context = new StandardEvaluationContext(auditDataSource);
    return this;
  }

  public LogEventBuilder withFields(String... fieldNames) {
    for (String fieldName : fieldNames) {
      try {
        data.put(fieldName, parser.parseExpression(fieldName).getValue(context));
      } catch (SpelEvaluationException e) {
        data.put(fieldName, "NOT PRESENT");
      }
    }
    return this;
  }

  public void log(){
    logEvent(data, event, log);
  }

  private void logEvent(Map<String, Object> data, AuditEvent event, Logger logger) {

  // Check log level to avoid serialising object.
    if (logger.isInfoEnabled()) {
      MDC.put("event", event.name());
      try {
        MDC.put("data", objectMapper.writer().writeValueAsString(data));
        logger.info(event.name());
      } catch (JsonProcessingException e) {
        logger.error("Failed writing audit message for event:" + event, e);
      } finally {
        MDC.remove("data");
        MDC.remove("event");
      }
    }
  }
}
