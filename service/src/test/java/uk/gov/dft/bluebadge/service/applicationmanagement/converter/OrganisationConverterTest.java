package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.VehicleConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class OrganisationConverterTest extends ApplicationFixture {

  @Mock private VehicleConverter vehicleConverter;

  private Application application;
  private ApplicationEntity entity = ApplicationEntity.builder().id(UUID.randomUUID()).build();
  private OrganisationConverter organisationConverter;

  public OrganisationConverterTest() {
    super();
    organisationConverter = new OrganisationConverter(vehicleConverter);
  }

  @Before
  public void initTest() {
    Mockito.reset(vehicleConverter);
  }

  @Test
  public void convertToEntity() {
    // Given a valid organisation app
    application =
        getApplicationBuilder().addBaseApplication().setOrganisation().addVehicle().build();

    // When converting
    organisationConverter.convertToEntity(application, entity);

    // Organisation specific data is populated
    verify(vehicleConverter, times(1))
        .convertToEntityList(
            application.getParty().getOrganisation().getVehicles(), entity.getId());
    assertEquals(ValidValues.CHARITY_NO, entity.getOrgCharityNo());
  }

  @Test
  public void convertToEntityWhenPerson() {
    // Given a person app (no organisation)
    application = getApplicationBuilder().addBaseApplication().setPerson().build();

    // When converting
    organisationConverter.convertToEntity(application, entity);

    // Nothing happens
    verify(vehicleConverter, never()).mapToEntity(any(), any());
  }
}
