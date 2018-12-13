package uk.gov.dft.bluebadge.service.applicationmanagement.controller;

import static uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository.DEFAULT_PAGE_NUM;
import static uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository.DEFAULT_PAGE_SIZE;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class PagingParams {

  @Min(1)
  private Integer pageNum = DEFAULT_PAGE_NUM;

  @Min(1)
  @Max(200)
  private Integer pageSize = DEFAULT_PAGE_SIZE;
}
