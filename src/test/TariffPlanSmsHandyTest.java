package test;

import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyHaveProviderException;
import main.smsHandy.model.Provider;
import main.smsHandy.model.TariffPlanSmsHandy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TariffPlanSmsHandyTest {

    @Test
    @DisplayName("Test for payForSms() TariffPlanSmsHandy")
    public void testPayForSmsPrepaidSmsHandy() throws ProviderNotFoundException, SmsHandyHaveProviderException {
        Provider provider = new Provider();
        provider.setName("Vodafone");
        TariffPlanSmsHandy handy = new TariffPlanSmsHandy("123", provider);

        handy.payForSms();
        assertEquals(99, handy.getRemainingFreeSms());
    }
}
