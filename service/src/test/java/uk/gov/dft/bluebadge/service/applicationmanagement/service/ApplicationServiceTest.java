package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationTestBase;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator;

public class ApplicationServiceTest extends ApplicationTestBase {

  @Mock private ApplicationRepository repository;
  @Mock private ApplicationConverter converter;

  private ApplicationService service;

  @Before
  public void setUp() {
    service = new ApplicationService(repository, converter);
  }

  @Test
  public void createApplication() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    entity.setId(UUID.randomUUID());
    when(converter.convertToEntityOnCreate(application)).thenReturn(entity);

    String result = service.createApplication(application);

    verify(converter, times(1)).convertToEntityOnCreate(application);
    verify(repository, times(1)).createApplication(entity);
    verify(repository, times(1)).createWalkingDifficultyTypes(any());
    Assert.notNull(result, "Should get id back");
  }
}
