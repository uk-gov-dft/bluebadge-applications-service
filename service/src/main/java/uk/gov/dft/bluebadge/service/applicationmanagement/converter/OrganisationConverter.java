package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Organisation;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.VehicleConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class OrganisationConverter {

  private final VehicleConverter vehicleConverter;

  @Autowired
  OrganisationConverter(VehicleConverter vehicleConverter) {
    this.vehicleConverter = vehicleConverter;
  }

  void convertToEntity(Application application, ApplicationEntity entity) {
    if (null != application.getParty().getOrganisation()) {
      Organisation organisation = application.getParty().getOrganisation();
      entity.setHolderName(organisation.getBadgeHolderName());
      entity.setOrgIsCharity(organisation.isIsCharity());
      entity.setOrgCharityNo(organisation.getCharityNumber());
      entity.setNoOfBadges(organisation.getNumberOfBadges());
      entity.setVehicles(
          vehicleConverter.convertToEntityList(organisation.getVehicles(), entity.getId()));
    }
  }
}
