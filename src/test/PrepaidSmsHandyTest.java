package test;

import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrepaidSmsHandyTest {

    @Test
    @DisplayName("Test Provider not found for Constructor PrepaidSmsHandy")
    public void testProviderNotFoundExceptionForConstructorPrepaidSmsHandy(){
        assertThrows(ProviderNotFoundException.class, () -> new PrepaidSmsHandy("123", null));
    }

    @Test
    @DisplayName("Test Provider not found for method-canSendSms PrepaidSmsHandy")
    public void testProviderNotFoundExceptionForCanSendSmsPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        prepaidSmsHandyO.setProvider(null);

        assertThrows(ProviderNotFoundException.class, prepaidSmsHandyO::canSendSms);
    }

    @Test
    @DisplayName("Test Provider not found for method-payForSms PrepaidSmsHandy")
    public void testProviderNotFoundExceptionForPayForSmsPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        prepaidSmsHandyO.setProvider(null);

        assertThrows(ProviderNotFoundException.class, prepaidSmsHandyO::payForSms);
    }

    @Test
    @DisplayName("Test Provider not found for method-deposit PrepaidSmsHandy")
    public void testProviderNotFoundExceptionForDepositPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        prepaidSmsHandyO.setProvider(null);

        assertThrows(ProviderNotFoundException.class, () -> prepaidSmsHandyO.deposit(50));
    }

    @Test
    @DisplayName("Test constructor PrepaidSmsHandy")
    public void testConstructorPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        assertEquals("123", prepaidSmsHandyO.getNumber());
        assertEquals(provider, prepaidSmsHandyO.getProvider());
        assertEquals(100, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
    }

    @Test
    @DisplayName("Test method-canSendSms PrepaidSmsHandy")
    public void testCanSendSmsPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        assertTrue(prepaidSmsHandyO.canSendSms());

        prepaidSmsHandyO.deposit(-91);
        assertFalse(prepaidSmsHandyO.canSendSms());
    }

    @Test
    @DisplayName("Test method-payForSms PrepaidSmsHandy")
    public void testPayForSmsPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);

        prepaidSmsHandyO.payForSms();
        assertEquals(90, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));

        prepaidSmsHandyO.deposit(-90);
        prepaidSmsHandyO.payForSms();
        assertEquals(0, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
    }

    @Test
    @DisplayName("Test method-deposit PrepaidSmsHandy")
    public void testDepositPrepaidSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        provider.setName("O!");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        assertEquals(100, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));

        prepaidSmsHandyO.deposit(100);
        assertEquals(200, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));

        prepaidSmsHandyO.deposit(-200);
        assertEquals(0, provider.getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
    }
}
