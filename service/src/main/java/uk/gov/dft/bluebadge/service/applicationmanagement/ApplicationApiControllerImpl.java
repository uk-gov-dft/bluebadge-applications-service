package uk.gov.dft.bluebadge.service.applicationmanagement;

import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.service.exception.ServiceException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.CreateApplicationResponse;
import uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller.ApplicationsApi;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.ApplicationService;

@Controller
public class ApplicationApiControllerImpl implements ApplicationsApi {

  private final ApplicationService service;

  @SuppressWarnings("unused")
  @Autowired
  public ApplicationApiControllerImpl(ApplicationService service) {
    this.service = service;
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @Override
  public ResponseEntity<CreateApplicationResponse> createApplication(
      @ApiParam() @Valid @RequestBody Application application) {

    String newId = service.createApplication(application);
    return ResponseEntity.ok(new CreateApplicationResponse().data(newId));
  }
}
