package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.collection.VehicleConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
class OrganisationConverter implements ApplicationBiConverter{

  private final VehicleConverter vehicleConverter;

  @Autowired
  OrganisationConverter(VehicleConverter vehicleConverter) {
    this.vehicleConverter = vehicleConverter;
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if(PartyTypeCodeField.ORG.name().equals(entity.getPartyCode())){
      if(null == model.getParty()){
        model.setParty(new Party());
      }
      if(null == model.getParty().getOrganisation()){
        model.getParty().setOrganisation(new Organisation());
      }
      Organisation organisation = model.getParty().getOrganisation();
      organisation.setBadgeHolderName(entity.getHolderName());
      organisation.setIsCharity(entity.getOrgIsCharity());
      organisation.setCharityNumber(entity.getOrgCharityNo());
      organisation.setNumberOfBadges(entity.getNoOfBadges());
      organisation.setVehicles(vehicleConverter.convertToModelList(entity.getVehicles()));
    }
  }

  public void convertToEntity(Application application, ApplicationEntity entity) {
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
