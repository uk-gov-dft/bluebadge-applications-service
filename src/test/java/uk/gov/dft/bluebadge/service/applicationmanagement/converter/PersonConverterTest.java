package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Person;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class PersonConverterTest extends ApplicationFixture {

  private PersonConverter converter = new PersonConverter();
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
    converter.convertToEntity(application, entity);

    // Then is successful
    assertEquals(new Integer(1), entity.getNoOfBadges());
    assertEquals(ValidValues.DOB, entity.getDob());
    // And nino is uppercase with no spaces
    assertEquals(ValidValues.NINO_FORMATTED, entity.getNino());
    assertEquals(ValidValues.BADGE_HOLDER_NAME, entity.getHolderName());
    assertEquals(ValidValues.BADGE_HOLDER_NAME_AT_BIRTH, entity.getHolderNameAtBirth());
    assertEquals(ValidValues.GENDER.name(), entity.getGenderCode());
  }

  @Test
  public void convertToEntity_WhenOrg() {
    // Given a valid org app
    application = getApplicationBuilder().addBaseApplication().setOrganisation().build();

    // When converting
    converter.convertToEntity(application, entity);

    // Nothing happens
    assertNull(entity.getDob());
  }

  @Test
  public void convertToEntity_nullNino() {
    application = getApplicationBuilder().addBaseApplication().setPerson().build();

    application.getParty().getPerson().setNino(null);

    // When converting
    converter.convertToEntity(application, entity);

    // no null pointer
    assertNull(entity.getNino());
  }

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model = new Application();

    converter.convertToModel(model, entity);
    Person person = model.getParty().getPerson();
    assertEquals(ValidValues.BADGE_HOLDER_NAME, person.getBadgeHolderName());
    assertEquals(ValidValues.NINO_FORMATTED, person.getNino());
    assertEquals(ValidValues.DOB, person.getDob());
    assertEquals(ValidValues.BADGE_HOLDER_NAME_AT_BIRTH, person.getNameAtBirth());
    assertEquals(ValidValues.GENDER, person.getGenderCode());
  }
}
