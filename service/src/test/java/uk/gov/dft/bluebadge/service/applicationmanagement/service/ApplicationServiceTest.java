package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationTestBase;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;

public class ApplicationServiceTest extends ApplicationTestBase {

  @Mock private ApplicationRepository repository;

  private ApplicationService service;

  @Before
  public void setUp() {
    service = new ApplicationService(repository);
  }

  @Test
  public void createApplication() {}
}
