package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Component
public class PartyConverter implements ApplicationBiConverter {

  private final List<ApplicationBiConverter> converters = new ArrayList<>();

  @Autowired
  PartyConverter(
      ContactConverter contactConverter,
      PersonConverter personConverter,
      OrganisationConverter organisationConverter) {
    converters.add(contactConverter);
    converters.add(personConverter);
    converters.add(organisationConverter);
  }

  @Override
  public void convertToModel(Application model, ApplicationEntity entity) {
    model.setParty(new Party());

    model.getParty().setTypeCode(PartyTypeCodeField.fromValue(entity.getPartyCode()));

    for (ApplicationBiConverter converter : converters) {
      converter.convertToModel(model, entity);
    }
  }

  @Override
  public void convertToEntity(Application model, ApplicationEntity entity) {
    // The following are NotNull annotated in beans and validation should have been done.
    Assert.notNull(
        model.getParty(),
        "Before converting Application validation should have failed for null party.");
    Assert.notNull(
        model.getParty().getContact(),
        "Before converting Application validation should have failed for null contact.");

    entity.setPartyCode(model.getParty().getTypeCode().name());
    for (ApplicationBiConverter converter : converters) {
      converter.convertToEntity(model, entity);
    }
  }
}
