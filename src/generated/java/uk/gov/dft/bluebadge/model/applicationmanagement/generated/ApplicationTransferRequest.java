package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ApplicationTransferRequest {
    @NotEmpty
    private String transferToLaShortCode;
}
