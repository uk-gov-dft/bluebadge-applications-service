package uk.gov.dft.bluebadge.service.applicationmanagement.controller;

import io.swagger.annotations.ApiParam;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.api.model.PagedResult;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationResponse;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummaryResponse;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTransferRequest;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationUpdate;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.CreateApplicationResponse;
import uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller.ApplicationsApi;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.ApplicationService;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator;

@RestController
@Slf4j
public class ApplicationApiController implements ApplicationsApi {

  private final ApplicationService service;
  private final ApplicationValidator validator;

  @SuppressWarnings("unused")
  @Autowired
  public ApplicationApiController(ApplicationService service, ApplicationValidator validator) {
    this.service = service;
    this.validator = validator;
  }

  @InitBinder("application")
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @Override
  public ResponseEntity<CreateApplicationResponse> createApplication(
      @ApiParam() @Valid @RequestBody Application application) {
    log.info("Creating application");
    UUID newId = service.createApplication(application);
    return ResponseEntity.ok(new CreateApplicationResponse().data(newId.toString()));
  }

  @Override
  public ResponseEntity<ApplicationSummaryResponse> findApplications(
      @Valid FindApplicationQueryParams searchParams, @Valid PagingParams pagingParams) {
    log.info("Find applications");

    PagedResult<ApplicationSummary> results = service.find(searchParams, pagingParams);
    return ResponseEntity.ok(
        (ApplicationSummaryResponse)
            new ApplicationSummaryResponse()
                .data(results.getData())
                .pagingInfo(results.getPagingInfo()));
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_VIEW_APPLICATION_DETAILS')")
  @PostAuthorize("@securityUtils.isAuthorisedLACode(returnObject.body.data.localAuthorityCode)")
  public ResponseEntity<ApplicationResponse> retrieveApplication(
      @ApiParam(required = true) @PathVariable("applicationId") String applicationId) {
    log.info("Retrieve application");
    Application result = service.retrieve(applicationId);
    return ResponseEntity.ok(new ApplicationResponse().data(result));
  }

  @Override
  @PreAuthorize(
      "hasAuthority('PERM_DELETE_APPLICATION') and @applicationSecurity.isAuthorised(#applicationId)")
  public ResponseEntity<Void> deleteApplication(
      @ApiParam(required = true) @PathVariable("applicationId") String applicationId) {
    log.info("Delete application");
    service.delete(applicationId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize(
      "hasAuthority('PERM_UPDATE_APPLICATION') and @applicationSecurity.isAuthorised(#applicationId)")
  public ResponseEntity<Void> updateApplication(
      @ApiParam(value = "", required = true) @PathVariable("applicationId") String applicationId,
      @ApiParam() @Valid @RequestBody ApplicationUpdate applicationUpdate) {
    service.update(applicationId, applicationUpdate);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize(
      "hasAuthority('PERM_UPDATE_APPLICATION') and @applicationSecurity.isAuthorised(#applicationId)")
  @RequestMapping(value = "/applications/{applicationId}/transfers", method = RequestMethod.POST)
  public ResponseEntity<Void> transferApplication(
      @PathVariable("applicationId") String applicationId,
      @Valid @RequestBody ApplicationTransferRequest applicationTransfer) {
    service.transferApplication(applicationId, applicationTransfer);
    return ResponseEntity.ok().build();
  }
}
