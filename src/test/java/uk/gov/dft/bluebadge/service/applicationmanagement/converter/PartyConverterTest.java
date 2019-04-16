package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PartyConverterTest extends ApplicationFixture {

  PartyConverter converter;

  @Mock PersonConverter personConverter;
  @Mock OrganisationConverter organisationConverter;
  @Mock ContactConverter contactConverter;

  public PartyConverterTest() {
    MockitoAnnotations.initMocks(this);
    converter = new PartyConverter(contactConverter, personConverter, organisationConverter);
  }

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model = new Application();

    converter.convertToModel(model, entity);
    assertEquals(ValidValues.PARTY, model.getParty().getTypeCode().name());
    verify(personConverter, times(1)).convertToModel(model, entity);
    verify(organisationConverter, times(1)).convertToModel(model, entity);
    verify(contactConverter, times(1)).convertToModel(model, entity);
  }

  @Test
  public void convertToEntity() {
    Application model = getApplicationBuilder().addBaseApplication().setPerson().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();

    converter.convertToEntity(model, entity);
    assertEquals(ValidValues.PARTY, entity.getPartyCode());
    verify(personConverter, times(1)).convertToEntity(model, entity);
    verify(organisationConverter, times(1)).convertToEntity(model, entity);
    verify(contactConverter, times(1)).convertToEntity(model, entity);
  }
}
