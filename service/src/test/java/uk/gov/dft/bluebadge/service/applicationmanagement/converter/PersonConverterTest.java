package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class PersonConverterTest extends ApplicationFixture {

  private PersonConverter personConverter = new PersonConverter();
  private Application application;
  private ApplicationEntity entity;

  @Before
  public void setUp() {
    entity = ApplicationEntity.builder().id(UUID.randomUUID()).build();
  }

  @Test
  public void convertToEntity() {
    // Given a valid application
    application = getApplicationBuilder().addBaseApplication().setPerson().build();
    // And nino has spaces in it
    application.getParty().getPerson().setNino("aa 12 34 56 A");

    // When converting
    personConverter.convertToEntity(application, entity);

    // Then is successful
    assertEquals(new Integer(1), entity.getNoOfBadges());
    assertEquals(ValidValues.DOB, entity.getDob());
    // And nino is uppercase with no spaces
    assertEquals("AA123456A", entity.getNino());
  }

  @Test
  public void convertToEntity_WhenOrg() {
    // Given a valid org app
    application = getApplicationBuilder().addBaseApplication().setOrganisation().build();

    // When converting
    personConverter.convertToEntity(application, entity);

    // Nothing happens
    assertNull(entity.getDob());
  }

  @Test
  public void convertToEntity_nullNino() {
    application = getApplicationBuilder().addBaseApplication().setPerson().build();

    application.getParty().getPerson().setNino(null);

    // When converting
    personConverter.convertToEntity(application, entity);

    // no null pointer
    assertNull(entity.getNino());
  }
}
