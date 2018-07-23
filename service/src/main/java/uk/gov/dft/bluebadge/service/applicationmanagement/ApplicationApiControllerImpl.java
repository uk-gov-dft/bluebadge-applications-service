package uk.gov.dft.bluebadge.service.applicationmanagement;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.ServiceException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.CreateApplicationResponse;
import uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller.ApplicationsApi;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.ApplicationService;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator;

@Controller
public class ApplicationApiControllerImpl implements ApplicationsApi {

  private final ApplicationService service;
  private ApplicationValidator validator;

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

  @SuppressWarnings("unused")
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({InvalidFormatException.class})
  public ResponseEntity<CommonResponse> handleInvalidFormatException(InvalidFormatException e) {
    Error error = new Error();
    error.setReason(e.getMessage());
    error.setMessage("InvalidFormat." + e.getTargetType().getSimpleName());
    BadRequestException e1 = new BadRequestException(error);
    return e1.getResponse();
  }

  @Override
  public ResponseEntity<CreateApplicationResponse> createApplication(
      @ApiParam() @Valid @RequestBody Application application) {

    String newId = service.createApplication(application);
    return ResponseEntity.ok(new CreateApplicationResponse().data(newId));
  }
}
