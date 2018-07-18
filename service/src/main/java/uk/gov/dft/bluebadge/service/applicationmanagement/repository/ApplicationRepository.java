package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.ApplicationMapper;

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
    return sqlSession.insert(CREATE, applicationEntity);
  }

  @Override
  public void createHealthcareProfessionals(List<HealthcareProfessionalEntity> professionals) {
    if (createRequired(professionals)) {
      int insertCount = sqlSession.insert(CREATE_HEALTHCARE_PROFESSIONALS, professionals);
      log.debug("{} healthcare professionals created.", insertCount);
    }
  }

  @Override
  public void createMedications(List<MedicationEntity> medications) {
    if (createRequired(medications)) {
      int insertCount = sqlSession.insert(CREATE_MEDICATIONS, medications);
      log.debug("{} medications created.", insertCount);
    }
  }

  @Override
  public void createTreatments(List<TreatmentEntity> treatments) {
    if (createRequired(treatments)) {
      int insertCount = sqlSession.insert(CREATE_TREATMENTS, treatments);
      log.debug("{} treatments created.", insertCount);
    }
  }

  @Override
  public void createVehicles(List<VehicleEntity> vehicles) {
    if (createRequired(vehicles)) {
      int insertCount = sqlSession.insert(CREATE_VEHICLES, vehicles);
      log.debug("{} vehicles created.", insertCount);
    }
  }

  @Override
  public void createWalkingAids(List<WalkingAidEntity> walkingAids) {
    if (createRequired(walkingAids)) {
      int insertCount = sqlSession.insert(CREATE_WALKING_AIDS, walkingAids);
      log.debug("{} walking aids created.", insertCount);
    }
  }

  @Override
  public void createWalkingDifficultyTypes(
      List<WalkingDifficultyTypeEntity> walkingDifficultyTypes) {
    if (createRequired(walkingDifficultyTypes)) {
      int insertCount = sqlSession.insert(CREATE_WALKING_DIFFICULTY_TYPES, walkingDifficultyTypes);
      log.debug("{} walking difficulties created.", insertCount);
    }
  }
}
