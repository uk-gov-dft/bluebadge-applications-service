package uk.gov.dft.bluebadge.service.applicationmanagement.repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationUpdate;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.BulkyEquipmentTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.ApplicationMapper;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper.Statements;

import java.util.List;

/** Provides CRUD operations on ApplicationEntity entity. */
@Component
@Slf4j
public class ApplicationRepository implements ApplicationMapper {

  public static final Integer DEFAULT_PAGE_NUM = 1;
  public static final Integer DEFAULT_PAGE_SIZE = 50;
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
  public int createBulkyEquipment(List<BulkyEquipmentTypeEntity> equipment) {
    int insertCount = 0;
    if (createRequired(equipment)) {
      insertCount = sqlSession.insert(Statements.CREATE_BULKY_EQUIPMENT_TYPES.getName(), equipment);
      log.debug("{} bulky equipments created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public int createArtifacts(List<ArtifactEntity> artifactEntities) {
    int insertCount = 0;
    if (createRequired(artifactEntities)) {
      insertCount = sqlSession.insert(Statements.CREATE_ARTIFACTS.getName(), artifactEntities);
      log.debug("{} artifacts created.", insertCount);
    }
    return insertCount;
  }

  @Override
  public List<ArtifactEntity> retrieveArtifacts(String applicationId) {
    return sqlSession.selectList(Statements.RETRIEVE_ARTIFACTS.getName(), applicationId);
  }

  @Override
  public List<ApplicationSummaryEntity> findApplications(
      FindApplicationQueryParams findApplicationQueryParams) {
    return findApplications(findApplicationQueryParams, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
  }

  @Override
  public Page<ApplicationSummaryEntity> findApplications(
      FindApplicationQueryParams findApplicationQueryParams, Integer pageNum, Integer pageSize) {
    Assert.notNull(findApplicationQueryParams, "findApplicationQueryParams is null");
    Assert.notNull(
        findApplicationQueryParams.getAuthorityCode(),
        "findApplicationQueryParams.authorityCode is null");
    Assert.notNull(pageNum, "pageNum is null");
    Assert.notNull(pageSize, "pageSize is null");
    return PageHelper.startPage(pageNum, pageSize, true)
        .doSelectPage(
            () -> sqlSession.selectList(Statements.FIND.getName(), findApplicationQueryParams));
  }

  @Override
  public ApplicationEntity retrieveApplication(RetrieveApplicationQueryParams params) {
    return sqlSession.selectOne(Statements.RETRIEVE.getName(), params);
  }

  @Override
  public int deleteApplication(RetrieveApplicationQueryParams params) {
    return sqlSession.update(Statements.UPDATE.getName(), params);
  }

  @Override
  public int deleteHealthcareProfessionals(String applicationId) {
    return sqlSession.delete(Statements.DELETE_HEALTHCARE_PROFESSIONALS.getName(), applicationId);
  }

  @Override
  public int deleteMedications(String applicationId) {
    return sqlSession.delete(Statements.DELETE_MEDICATIONS.getName(), applicationId);
  }

  @Override
  public int deleteTreatments(String applicationId) {
    return sqlSession.delete(Statements.DELETE_TREATMENTS.getName(), applicationId);
  }

  @Override
  public int deleteVehicles(String applicationId) {
    return sqlSession.delete(Statements.DELETE_VEHICLES.getName(), applicationId);
  }

  @Override
  public int deleteWalkingAids(String applicationId) {
    return sqlSession.delete(Statements.DELETE_WALKING_AIDS.getName(), applicationId);
  }

  @Override
  public int deleteWalkingDifficultyTypes(String applicationId) {
    return sqlSession.delete(Statements.DELETE_WALKING_DIFFICULTY_TYPES.getName(), applicationId);
  }

  @Override
  public int deleteBulkyEquipmentTypes(String applicationId) {
    return sqlSession.delete(Statements.DELETE_BULKY_EQUIPMENT_TYPES.getName(), applicationId);
  }

  @Override
  public int deleteArtifacts(String applicationId) {
    return sqlSession.delete(Statements.DELETE_ARTIFACTS.getName(), applicationId);
  }

  @Override
  public int updateApplication(ApplicationUpdate applicationUpdate) {
    return sqlSession.update(Statements.UPDATE_APPLICATION.getName(), applicationUpdate);
  }
}
