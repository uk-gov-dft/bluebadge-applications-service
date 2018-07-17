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
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller.ApplicationsApi;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.ApplicationService;

@Controller
public class ApplicationApiControllerImpl implements ApplicationsApi {

  private final ApplicationService service;
  private ApplicationConverter applicationConverter;

  @SuppressWarnings("unused")
  @Autowired
  public ApplicationApiControllerImpl(
      ApplicationService service, ApplicationConverter applicationConverter) {
    this.service = service;
    this.applicationConverter = applicationConverter;
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @Override
  public ResponseEntity<CreateApplicationResponse> createApplication(
      @ApiParam() @Valid @RequestBody Application application) {

    ApplicationEntity entity = applicationConverter.convertToEntityOnCreate(application);
    service.createApplication(entity);
    return ResponseEntity.ok(new CreateApplicationResponse().data(entity.getId().toString()));
  }
}
