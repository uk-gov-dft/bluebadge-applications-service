package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.ApplicationMapper;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.Statements;

/** Provides CRUD operations on ApplicationEntity entity. */
@Component
@Slf4j
public class ApplicationRepository implements ApplicationMapper {

  private final SqlSession sqlSession;

  ApplicationRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  private boolean createRequired(List entities) {
    return null != entities && !entities.isEmpty();
  }

  @Override
  public int createApplication(ApplicationEntity applicationEntity) {
    return sqlSession.insert(Statements.CREATE.getName(), applicationEntity);
  }

  @Override
  public int createHealthcareProfessionals(List<HealthcareProfessionalEntity> professionals) {
    int insertCount = 0;
    if (createRequired(professionals)) {
      insertCount =
          sqlSession.insert(Statements.CREATE_HEALTHCARE_PROFESSIONALS.getName(), professionals);
      log.debug("{} healthcare professionals created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createMedications(List<MedicationEntity> medications) {
    int insertCount = 0;
    if (createRequired(medications)) {
      insertCount = sqlSession.insert(Statements.CREATE_MEDICATIONS.getName(), medications);
      log.debug("{} medications created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createTreatments(List<TreatmentEntity> treatments) {
    int insertCount = 0;
    if (createRequired(treatments)) {
      insertCount = sqlSession.insert(Statements.CREATE_TREATMENTS.getName(), treatments);
      log.debug("{} treatments created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createVehicles(List<VehicleEntity> vehicles) {
    int insertCount = 0;
    if (createRequired(vehicles)) {
      insertCount = sqlSession.insert(Statements.CREATE_VEHICLES.getName(), vehicles);
      log.debug("{} vehicles created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createWalkingAids(List<WalkingAidEntity> walkingAids) {
    int insertCount = 0;
    if (createRequired(walkingAids)) {
      insertCount = sqlSession.insert(Statements.CREATE_WALKING_AIDS.getName(), walkingAids);
      log.debug("{} walking aids created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createWalkingDifficultyTypes(
      List<WalkingDifficultyTypeEntity> walkingDifficultyTypes) {
    int insertCount = 0;
    if (createRequired(walkingDifficultyTypes)) {
      insertCount =
          sqlSession.insert(
              Statements.CREATE_WALKING_DIFFICULTY_TYPES.getName(), walkingDifficultyTypes);
      log.debug("{} walking difficulties created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public List<ApplicationSummaryEntity> findApplications(
      FindApplicationQueryParams findApplicationQueryParams) {
    return sqlSession.selectList(Statements.FIND.getName(), findApplicationQueryParams);
  }
}
