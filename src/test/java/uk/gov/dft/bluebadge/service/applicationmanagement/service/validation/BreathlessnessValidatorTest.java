package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

import org.junit.Test;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;

import static org.junit.Assert.assertEquals;

public class BreathlessnessValidatorTest extends ApplicationFixture {
    private BreathlessnessValidator breathlessnessValidator = new BreathlessnessValidator();

    @Test
    public void validate() {
        // Valid first
        reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().addBreathlessness().build());
        breathlessnessValidator.validate(app, errors);
        assertEquals(0, errors.getErrorCount());

        // Invalid second
        reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().addBreathlessnessWithoutTypecode().build());
        breathlessnessValidator.validate(app, errors);
        assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_WALKING_BREATHLESSNESS_TYPES));
    }

    @Test
    public void validateBreathlessnessOtherDescription() {
        // Valid first
        reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().addBreathlessnessOther().build());
        breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
        assertEquals(0, errors.getErrorCount());
        assertEquals(0, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));

        // Invalid second
        reset(getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWalking().addBreathlessnessOtherDescriptionOnly().build());
        breathlessnessValidator.validateBreathlessnessOtherDescription(app, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals(1, errors.getFieldErrorCount(FieldKeys.KEY_ELI_BREATHLESSNESS_OTHER_DESC));
    }
}