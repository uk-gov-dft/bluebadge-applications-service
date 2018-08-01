package uk.gov.dft.bluebadge.service.applicationmanagement;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
  classes = ApplicationManagementApplication.class,
  properties = {"management.server.port=19991"}
)
@ActiveProfiles({"test", "dev"})
public class ApplicationContextTests {
  @Test
  public void loadContext() {}
}
