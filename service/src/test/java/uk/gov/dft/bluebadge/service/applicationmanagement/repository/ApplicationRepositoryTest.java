package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.Statements;

public class ApplicationRepositoryTest {

  @Mock private SqlSession sqlSession;

  private ApplicationRepository repository;

  public ApplicationRepositoryTest() {
    MockitoAnnotations.initMocks(this);
    repository = new ApplicationRepository(sqlSession);
  }

  @Test
  public void createApplication() {
    repository.createApplication(ApplicationEntity.builder().build());
    verify(sqlSession).insert(eq(Statements.CREATE.getName()), any());
  }
}