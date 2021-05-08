package test;

import main.smsHandy.exception.InvalidNumberException;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;
import main.smsHandy.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SmsHandyTest {
    private Provider provider1;
    private Provider provider2;
    private PrepaidSmsHandy prepaidSmsHandyTelekom;
    private PrepaidSmsHandy prepaidSmsHandyTelekom2;
    private PrepaidSmsHandy prepaidSmsHandyBeeline;
    TariffPlanSmsHandy tariffSmsHandyTelekom;
    TariffPlanSmsHandy tariffSmsHandyTelekom2;
    TariffPlanSmsHandy tariffSmsHandyBeeline;

    @BeforeEach
    public void init() throws ProviderNotFoundException {
        provider1 = new Provider();
        provider2 = new Provider();
        provider1.setName("O!");
        provider1.setName("Beeline");
        prepaidSmsHandyTelekom = new PrepaidSmsHandy("123", provider1);
        prepaidSmsHandyTelekom2 = new PrepaidSmsHandy("345", provider1);
        prepaidSmsHandyBeeline = new PrepaidSmsHandy("222", provider2);
        tariffSmsHandyTelekom = new TariffPlanSmsHandy("001", provider1);
        tariffSmsHandyBeeline = new TariffPlanSmsHandy("002", provider2);

    }
    // TODO: 07.05.2021 BeforeEach nutzen

    @Test
    @DisplayName("Test method-sendSms SmsHandy")
    public void testSendSmsSmsHandy() throws ProviderNotFoundException {
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
        prepaidSmsHandyTelekom.sendSmsDirect(prepaidSmsHandyTelekom2, "Direct message");
        assertEquals(100, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        tariffSmsHandyTelekom.sendSmsDirect(tariffSmsHandyBeeline, "Some message");
        assertEquals(100, tariffSmsHandyTelekom.getRemainingFreeSms());
        assertThrows(SmsHandyNotFoundException.class, () -> prepaidSmsHandyTelekom.sendSmsDirect(null, "Hi"));

    }

    @Test
    @DisplayName("Test method-receiveSms SmsHandy")
    public void testReceiveSmsSmsHandy() {
        Message message = new Message();
        message.setFrom(null);
        message.setTo(prepaidSmsHandyTelekom2.getNumber());
        message.setDate(new Date());
        message.setContent("Hallo");
        prepaidSmsHandyTelekom.receiveSms(null);
        assertNotNull(message, "SmsHandy can't be null");
    }

}
