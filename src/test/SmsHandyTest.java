package test;

import main.smsHandy.exception.InvalidNumberException;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;
import main.smsHandy.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SmsHandyTest {

    @Test
    @DisplayName("Test method-sendSms SmsHandy")
    public void testSendSmsSmsHandy() throws ProviderNotFoundException {

        Provider provider = new Provider();
        Provider provider1 = new Provider();
        provider.setName("O!");
        provider1.setName("Beeline");
        PrepaidSmsHandy prepaidSmsHandyTelekom = new PrepaidSmsHandy("123", provider);
        PrepaidSmsHandy prepaidSmsHandyTelekom2 = new PrepaidSmsHandy("345", provider);
        PrepaidSmsHandy prepaidSmsHandyBeeline = new PrepaidSmsHandy("222", provider1);

        prepaidSmsHandyTelekom.sendSms("*101#", "");
        assertEquals(100, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom.sendSms(prepaidSmsHandyTelekom2.getNumber(), "Hallo");
        assertEquals(90, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom.sendSms(prepaidSmsHandyBeeline.getNumber(), "Haaaaallo");
        assertEquals(80, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        assertThrows(InvalidNumberException.class, () -> prepaidSmsHandyTelekom.sendSms(prepaidSmsHandyTelekom.getNumber(), "Trying to send to the same number"));
    }

    @Test
    @DisplayName("Test method-sendSmsDirect SmsHandy")
    public void testSendSmsDirectSmsHandy() throws ProviderNotFoundException {

        Provider provider = new Provider();
        Provider provider1 = new Provider();
        provider.setName("O!");
        provider1.setName("Beeline");
        PrepaidSmsHandy prepaidSmsHandyTelekom = new PrepaidSmsHandy("123", provider);
        PrepaidSmsHandy prepaidSmsHandyTelekom2 = new PrepaidSmsHandy("345", provider);
        TariffPlanSmsHandy tariffSmsHandyTelekom = new TariffPlanSmsHandy("001", provider);
        TariffPlanSmsHandy tariffSmsHandyBeeline = new TariffPlanSmsHandy("002", provider1);
        prepaidSmsHandyTelekom.sendSmsDirect(prepaidSmsHandyTelekom2, "Direct message");
        assertEquals(100, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        tariffSmsHandyTelekom.sendSmsDirect(tariffSmsHandyBeeline, "Some message");
        assertEquals(100, tariffSmsHandyTelekom.getRemainingFreeSms());
        assertThrows(SmsHandyNotFoundException.class, () -> prepaidSmsHandyTelekom.sendSmsDirect(null, "Hi"));

    }

    @Test
    @DisplayName("Test method-receiveSms SmsHandy")
    public void testReceiveSmsSmsHandy() throws ProviderNotFoundException {
        Provider provider = new Provider();
        PrepaidSmsHandy prepaidSmsHandyTelekom = new PrepaidSmsHandy("123", provider);
        PrepaidSmsHandy prepaidSmsHandyTelekom2 = new PrepaidSmsHandy("345", provider);
        Message message = new Message();
        message.setFrom(null);
        message.setTo(prepaidSmsHandyTelekom2.getNumber());
        message.setDate(new Date());
        message.setContent("Hallo");
        prepaidSmsHandyTelekom.receiveSms(null);
        assertNotNull(message, "SmsHandy can't be null");
    }

}
