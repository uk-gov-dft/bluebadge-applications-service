package uk.gov.dft.bluebadge.service.applicationmanagement.generated.controller;

public class NotFoundException extends ApiException {
  private int code;

  public NotFoundException(int code, String msg) {
    super(code, msg);
    this.code = code;
  }
}
