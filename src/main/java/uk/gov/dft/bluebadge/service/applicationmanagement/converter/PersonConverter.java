package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.GenderCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Person;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
class PersonConverter implements ApplicationBiConverter {

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    if (PartyTypeCodeField.PERSON.name().equals(entity.getPartyCode())) {
      if (null == model.getParty()) {
        model.setParty(new Party());
      }
      if (null == model.getParty().getPerson()) {
        model.getParty().setPerson(new Person());
      }
      Person person = model.getParty().getPerson();
      person.setBadgeHolderName(entity.getHolderName());
      person.setNino(entity.getNino());
      person.setDob(entity.getDob());
      person.setNameAtBirth(entity.getHolderNameAtBirth());
      person.setGenderCode(GenderCodeField.fromValue(entity.getGenderCode()));
    }
  }

  @Override
  public void convertToEntity(Application application, ApplicationEntity entity) {
    if (null != application.getParty().getPerson()) {
      Person person = application.getParty().getPerson();
      entity.setHolderName(person.getBadgeHolderName());
      if (null != person.getNino()) {
        entity.setNino(StringUtils.removeAll(person.getNino().toUpperCase(), " "));
      }
      entity.setDob(person.getDob());
      entity.setHolderNameAtBirth(person.getNameAtBirth());
      entity.setGenderCode(person.getGenderCode().toString());
      entity.setNoOfBadges(1);
    }
  }
}
