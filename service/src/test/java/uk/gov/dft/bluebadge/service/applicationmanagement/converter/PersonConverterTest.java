package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.then;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import java.util.UUID;

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

    // When converting
    personConverter.convertToEntity(application, entity);

    // Then is successful
    assertEquals(new Integer(1), entity.getNoOfBadges());
    assertEquals(ValidValues.DOB, entity.getDob());
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
}
