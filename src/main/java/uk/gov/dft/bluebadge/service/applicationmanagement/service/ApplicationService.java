package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.security.Permissions;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationSummaryConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;

@Slf4j
@Service
@Transactional
public class ApplicationService {

	private final ApplicationRepository repository;
	private final ApplicationConverter converter;
	private final SecurityUtils securityUtils;

	@Autowired
	ApplicationService(ApplicationRepository repository, ApplicationConverter converter, SecurityUtils securityUtils) {
		this.repository = repository;
		this.converter = converter;
		this.securityUtils = securityUtils;
	}

	/**
	 * Creates an Application given a VALIDATED Application.
	 *
	 * @param applicationModel
	 *            A validated application.
	 * @return A UUID for the newly created Application.
	 */
	public UUID createApplication(Application applicationModel) {

		addUuid(applicationModel);

		// For create set submission date to now
		applicationModel.setSubmissionDate(OffsetDateTime.now(Clock.systemUTC()));
		log.debug("Submission date set to:{}", applicationModel.getSubmissionDate());

		ApplicationEntity application = converter.convertToEntity(applicationModel);

		int insertCount;
		log.info("Creating application: {}", application.getId());
		insertCount = repository.createApplication(application);
		log.debug("{} applications created", insertCount);

		repository.createHealthcareProfessionals(application.getHealthcareProfessionals());
		repository.createMedications(application.getMedications());
		repository.createTreatments(application.getTreatments());
		repository.createVehicles(application.getVehicles());
		repository.createWalkingAids(application.getWalkingAids());
		repository.createWalkingDifficultyTypes(application.getWalkingDifficultyTypes());
		return application.getId();
	}

	private void addUuid(Application applicationModel) {
		// Set a UUID if it doesn't have one
		if (null == applicationModel.getApplicationId()) {
			applicationModel.setApplicationId(UUID.randomUUID().toString());
		}
	}

	public List<ApplicationSummary> find(String name, String postcode, OffsetDateTime from, OffsetDateTime to,
			String applicationTypeCode) {

		String userAuthorityCode = securityUtils.getCurrentLocalAuthorityShortCode();

		if (null == userAuthorityCode) {
			Error error = new Error();
			error.setMessage("Expected logged in user to have an LA code for find application.");
			error.setReason("localAuthorityShortCode");
			throw new BadRequestException(error);
		}

		ApplicationTypeCodeField applicationTypeCodeEnum = ApplicationTypeCodeField.fromValue(applicationTypeCode);
		if (StringUtils.isNotBlank(applicationTypeCode) && applicationTypeCodeEnum == null) {
			Error error = new Error();
			error.setMessage("Invalid applicationTypeCode: " + applicationTypeCode);
			error.setReason("applicationTypeCode");
			throw new BadRequestException(error);
		}

		FindApplicationQueryParams params = FindApplicationQueryParams.builder().authorityCode(userAuthorityCode)
				.name(name).applicationTypeCode(applicationTypeCodeEnum).submissionTo(timeToInstantOrNull(to))
				.submissionFrom(timeToInstantOrNull(from)).postcode(postcode).build();
		return new ApplicationSummaryConverter().convertToModelList(repository.findApplications(params));
	}

	private Instant timeToInstantOrNull(OffsetDateTime time) {
		if (null == time) {
			return null;
		}

		return time.toInstant();
	}

	public Application retrieve(String applicationId) {
		UUID uuid = getUuid(applicationId);

		ApplicationEntity entity = repository.retrieveApplication(RetrieveApplicationQueryParams.builder().uuid(uuid)
				.authorityCode(securityUtils.getCurrentLocalAuthorityShortCode()).build());
		if (null == entity) {
			throw new NotFoundException("application", NotFoundException.Operation.RETRIEVE);
		}
		return converter.convertToModel(entity);
	}

	public void delete(String applicationId) {
		log.debug("Deleting application with id: {}", applicationId);
		UUID uuid = getUuid(applicationId);
		
		if (securityUtils.isPermitted(Permissions.DELETE_APPLICATION)) {
			log.debug("Logged in user has permission to delete an application");
			RetrieveApplicationQueryParams params = RetrieveApplicationQueryParams.builder().uuid(uuid).authorityCode(securityUtils.getCurrentLocalAuthorityShortCode()).build();
			boolean canDelete = repository.deleteApplication(params) == 1;
			if (canDelete) {
				log.debug("Logged in user has permission to delete an application and it belongs to user's local authority");
				repository.deleteHealthcareProfessionals(uuid);
				repository.deleteMedications(uuid);
				repository.deleteTreatments(uuid);
				repository.deleteVehicles(uuid);
				repository.deleteWalkingAids(uuid);
				repository.deleteWalkingDifficultyTypes(uuid);
				log.debug("Application {} has been deleted", applicationId);
			} else {
				log.debug("Either application with id {} doesn't exist or it doesn't belong to user's local authority {}", applicationId, securityUtils.getCurrentLocalAuthorityShortCode());				
				throw new NotFoundException("application", NotFoundException.Operation.DELETE);
			} 
		} else {
			log.debug("User {} doesn't have permission to delete applications", securityUtils.getCurrentAuth().getEmailAddress());
		}
	}

	private UUID getUuid(String applicationId) {
		UUID uuid;
		try {
			uuid = UUID.fromString(applicationId);
		} catch (IllegalArgumentException e) {
			Error error = new Error();
			error.setReason("Invalid uuid in query param applicationId");
			throw new BadRequestException(error);
		}

		return uuid;
	}
}
