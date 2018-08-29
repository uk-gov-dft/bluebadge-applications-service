package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.converter.ToEntityFormatter;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class ContactConverter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {

    if(null == model.getParty()){
      model.setParty(new Party());
    }
    if(null == model.getParty().getContact()){
      model.getParty().setContact(new Contact());
    }
    Contact contact = model.getParty().getContact();
    contact.setFullName(entity.getContactName());
    contact.setBuildingStreet(entity.getContactBuildingStreet());
    contact.setLine2(entity.getContactLine2());
    contact.setTownCity(entity.getContactTownCity());
    contact.setEmailAddress(entity.getContactEmailAddress());
    contact.setPostCode(entity.getContactPostcode());
    contact.setPrimaryPhoneNumber(entity.getPrimaryPhoneNo());
    contact.setSecondaryPhoneNumber(entity.getSecondaryPhoneNo());
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    Contact contact = model.getParty().getContact();
    entity.setContactName(contact.getFullName());
    entity.setContactBuildingStreet(contact.getBuildingStreet());
    entity.setContactLine2(contact.getLine2());
    entity.setContactTownCity(contact.getTownCity());
    entity.setContactEmailAddress(contact.getEmailAddress());
    entity.setContactPostcode(ToEntityFormatter.postcode(contact.getPostCode()));
    entity.setPrimaryPhoneNo(contact.getPrimaryPhoneNumber());
    entity.setSecondaryPhoneNo(contact.getSecondaryPhoneNumber());
  }
}
