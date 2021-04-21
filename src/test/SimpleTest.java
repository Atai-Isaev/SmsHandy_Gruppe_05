package test;

import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    @DisplayName("Simple multiplication should work")
    public void someTest() {
        Provider provider = new Provider();
        Provider provider1 = new Provider();
        provider.setName("O!");
        provider1.setName("Beeline");
        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider);
        PrepaidSmsHandy prepaidSmsHandyO2 = new PrepaidSmsHandy("345", provider);
        PrepaidSmsHandy prepaidSmsHandyBeeline = new PrepaidSmsHandy("222", provider1);
        TariffPlanSmsHandy tariffSmsHandyO = new TariffPlanSmsHandy("001", provider);
        TariffPlanSmsHandy tariffSmsHandyBeeline = new TariffPlanSmsHandy("002", provider1);


        prepaidSmsHandyO.sendSms("*101#", "");
        assertEquals(100, prepaidSmsHandyO.getProvider().getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
        prepaidSmsHandyO.sendSms(prepaidSmsHandyO2.getNumber(), "Hallo");
        assertEquals(90, prepaidSmsHandyO.getProvider().getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
        prepaidSmsHandyO2.listReceived();
        prepaidSmsHandyO.sendSms(prepaidSmsHandyBeeline.getNumber(), "Haaaaallo");
        assertEquals(80, prepaidSmsHandyO.getProvider().getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
        prepaidSmsHandyBeeline.listReceived();

        prepaidSmsHandyO.sendSmsDirect(prepaidSmsHandyO2, "Direct message");
        assertEquals(80, prepaidSmsHandyO.getProvider().getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
        prepaidSmsHandyO.listSent();
        prepaidSmsHandyO2.listReceived();
        prepaidSmsHandyO.sendSmsDirect(prepaidSmsHandyBeeline, "Direct message");
        assertEquals(80, prepaidSmsHandyO.getProvider().getCreditForSmsHandy(prepaidSmsHandyO.getNumber()));
        prepaidSmsHandyO.listSent();
        prepaidSmsHandyBeeline.listReceived();

//        tariffSmsHandyO.sendSms("", "Tarrifsms");
//        String str = "Please choose a valid phone number";
//        System.out.println(str);
//        assertEquals(str, outContent.toString());
//        tariffSmsHandyO.sendSms("001", "");
//        assertEquals(str, outContent.toString());
        tariffSmsHandyO.sendSms("002", "Hallo aus dem anderem Seit!");
        tariffSmsHandyO.listSent();
        assertEquals(99, tariffSmsHandyO.getRemainingFreeSms());
        tariffSmsHandyBeeline.listReceived();
    }


}
