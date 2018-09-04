package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

public class ContactConverterTest extends ApplicationFixture {

  private ContactConverter converter = new ContactConverter();

  @Test
  public void convertToModel() {
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model = new Application();
    model.setParty(new Party());
    converter.convertToModel(model, entity);

    Contact contact = model.getParty().getContact();
    Assert.assertEquals(contact.getBuildingStreet(), ValidValues.CONTACT_BUILDING);
    Assert.assertEquals(contact.getEmailAddress(), ValidValues.CONTACT_EMAIL);
    Assert.assertEquals(contact.getFullName(), ValidValues.CONTACT_NAME);
    Assert.assertEquals(contact.getLine2(), ValidValues.LINE2);
    Assert.assertEquals(contact.getPostCode(), ValidValues.POSTCODE);
    Assert.assertEquals(contact.getPrimaryPhoneNumber(), ValidValues.PHONE_NO);
    Assert.assertEquals(contact.getSecondaryPhoneNumber(), ValidValues.PHONE_NO);
    Assert.assertEquals(contact.getTownCity(), ValidValues.TOWN);
  }

  @Test
  public void convertToEntity() {
    ApplicationEntity entity = ApplicationEntity.builder().build();
    Application model = getApplicationBuilder().addBaseApplication().build();
    converter.convertToEntity(model, entity);

    Assert.assertEquals(entity.getContactBuildingStreet(), ValidValues.CONTACT_BUILDING);
    Assert.assertEquals(entity.getContactEmailAddress(), ValidValues.CONTACT_EMAIL);
    Assert.assertEquals(entity.getContactName(), ValidValues.CONTACT_NAME);
    Assert.assertEquals(entity.getContactLine2(), ValidValues.LINE2);
    Assert.assertEquals(entity.getContactPostcode(), ValidValues.POSTCODE_FORMATTED);
    Assert.assertEquals(entity.getPrimaryPhoneNo(), ValidValues.PHONE_NO);
    Assert.assertEquals(entity.getSecondaryPhoneNo(), ValidValues.PHONE_NO);
    Assert.assertEquals(entity.getContactTownCity(), ValidValues.TOWN);
  }
}
