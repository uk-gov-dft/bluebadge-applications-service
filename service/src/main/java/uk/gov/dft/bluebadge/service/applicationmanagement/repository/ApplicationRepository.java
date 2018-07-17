package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.ApplicationMapper;

/** Provides CRUD operations on ApplicationEntity entity. */
@Component
@Slf4j
public class ApplicationRepository implements ApplicationMapper {

  class Statements {
    private Statements() {}

    static final String CREATE = "createApplication";
  }

  private final SqlSession sqlSession;

  public ApplicationRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public void createApplication(ApplicationEntity applicationEntity) {
    sqlSession.insert(Statements.CREATE, applicationEntity);
  }

  @Override
  public int createHealthcareProfessionals(List<HealthcareProfessionalEntity> professionals) {
    // TODO
    return 0;
  }
}
