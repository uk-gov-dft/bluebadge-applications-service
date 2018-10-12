package uk.gov.dft.bluebadge.service.applicationmanagement.controller;

import io.swagger.annotations.ApiParam;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.controller.AbstractController;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationResponse;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummaryResponse;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.CreateApplicationResponse;
import uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller.ApplicationsApi;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.ApplicationService;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator;

@RestController
public class ApplicationApiControllerImpl extends AbstractController implements ApplicationsApi {

  private final ApplicationService service;
  private final ApplicationValidator validator;

  @SuppressWarnings("unused")
  @Autowired
  public ApplicationApiControllerImpl(ApplicationService service, ApplicationValidator validator) {
    this.service = service;
    this.validator = validator;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @Override
  public ResponseEntity<CreateApplicationResponse> createApplication(
      @ApiParam() @Valid @RequestBody Application application) {

    UUID newId = service.createApplication(application);
    return ResponseEntity.ok(new CreateApplicationResponse().data(newId.toString()));
  }

  @Override
  public ResponseEntity<ApplicationSummaryResponse> findApplications(
      @ApiParam(value = "'Search by organisation or person name, results contain search param' ")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name,
      @ApiParam(value = "'Returns results starting with the parameter.' ")
          @Valid
          @RequestParam(value = "postcode", required = false)
          Optional<String> postcode,
      @ApiParam(value = "From submission date inclusive. 2018-12-25T12:30:45Z")
          @Valid
          @RequestParam(value = "from", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          Optional<OffsetDateTime> from,
      @ApiParam(value = "To submission date inclusive. 2018-12-25T12:30:45Z")
          @Valid
          @RequestParam(value = "to", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          Optional<OffsetDateTime> to,
      @ApiParam(allowableValues = "NEW, RENEW, CANCEL, REVOKE")
          @Valid
          @RequestParam(value = "applicationTypeCode", required = false)
          Optional<String> applicationTypeCode) {

    List<ApplicationSummary> results =
        service.find(
            name.orElse(null),
            postcode.orElse(null),
            from.orElse(null),
            to.orElse(null),
            applicationTypeCode.orElse(null));
    return ResponseEntity.ok(new ApplicationSummaryResponse().data(results));
  }

  @Override
  public ResponseEntity<ApplicationResponse> retrieveApplication(
      @ApiParam(required = true) @PathVariable("applicationId") String applicationId) {

    Application result = service.retrieve(applicationId);
    return ResponseEntity.ok(new ApplicationResponse().data(result));
  }

	@Override
  @PreAuthorize("hasAuthority('DELETE_APPLICATION') and @applicationSecurity.isAuthorised(#applicationId)")
	public ResponseEntity<Void> deleteApplication(
			@ApiParam(value = "", required = true) @PathVariable("applicationId") String applicationId) {

		service.delete(applicationId);
		return ResponseEntity.ok().build();
	}
  
  
}
