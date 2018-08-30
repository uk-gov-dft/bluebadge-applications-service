package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;

public class ApplicationServiceTest extends ApplicationFixture {

  private static final String INVALID_APPLICATION_TYPE_CODE = "WRONG";

  @Mock private ApplicationRepository repository;
  @Mock private ApplicationConverter converter;
  @Mock SecurityUtils securityUtils;
  private ApplicationService service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new ApplicationService(repository, converter, securityUtils);
  }

  @Test
  public void createApplication() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    application.setSubmissionDate(null);
    application.setApplicationId(null);

    entity.setId(UUID.randomUUID());
    when(converter.convertToEntity(application)).thenReturn(entity);

    UUID result = service.createApplication(application);

    verify(converter, times(1)).convertToEntity(application);
    verify(repository, times(1)).createApplication(entity);
    verify(repository, times(1)).createWalkingDifficultyTypes(any());
    assertNotNull("Should get id back", result);

    assertNotNull("Submission date set as part of create", application.getSubmissionDate());
    assertNotNull("Id set as part of create", application.getApplicationId());
  }

  @Test(expected = BadRequestException.class)
  public void find_nullAuthorityCode() {
    // Given user with no local authority.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn(null);

    // When find.
    service.find(null, null, null, null, null);

    // Then Bad Request thrown.
  }

  @Test(expected = BadRequestException.class)
  public void find_invalidApplicationTypeCode() {
    // Given user with no local authority.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");

    // When find.
    service.find(null, null, null, null, INVALID_APPLICATION_TYPE_CODE);

    // Then Bad Request thrown.
  }

  @Test
  public void find_withNullsValid() {
    // Given a search for applications with valid LA and no other criteria.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    when(repository.findApplications(any())).thenReturn(new ArrayList<>());

    // When searching
    service.find(null, null, null, null, null);

    // No exceptions
  }

  @Test
  public void find_validAndResults() {
    // Given a search for applications with all criteria specified
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    List<ApplicationSummaryEntity> entities = new ArrayList<>();
    entities.add(ApplicationSummaryEntity.builder().applicationId(UUID.randomUUID()).build());
    when(repository.findApplications(any())).thenReturn(entities);

    // When searching
    List<ApplicationSummary> results =
        service.find("name", "postcode", OffsetDateTime.now(), OffsetDateTime.now(), "NEW");

    // Then valid converted model object returned.
    assertEquals(1, results.size());
    assertNotNull(results.get(0).getApplicationId());
  }

  @Test
  public void retrieve() {
    String uuid = UUID.randomUUID().toString();
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWpms().build();

    when(repository.retrieveApplication(any())).thenReturn(entity);
    when(converter.convertToModel(entity)).thenReturn(model);

    Application a = service.retrieve(uuid);

    assertEquals(model, a);
  }

  @Test(expected = BadRequestException.class)
  public void retrieve_invalidUuid() {
    service.retrieve("ABC");
  }

  @Test(expected = NotFoundException.class)
  public void retrieve_noResult() {
    when(repository.retrieveApplication(any())).thenReturn(null);
    service.retrieve(UUID.randomUUID().toString());
  }
}
