package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Person;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
class PersonConverter {

  void convertToEntity(Application application, ApplicationEntity entity) {
    if (null != application.getParty().getPerson()) {
      Person person = application.getParty().getPerson();
      entity.setHolderName(person.getBadgeHolderName());
      entity.setNino(StringUtils.removeAll(person.getNino().toUpperCase(), " "));
      entity.setDob(person.getDob());
      entity.setHolderNameAtBirth(person.getNameAtBirth());
      entity.setGenderCode(person.getGenderCode().toString());
      entity.setNoOfBadges(1);
    }
  }
}
