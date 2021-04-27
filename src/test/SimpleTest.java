package test;

import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import main.smsHandy.model.TariffPlanSmsHandy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    private final PrintStream saveOut = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void restore() {
        System.setOut(saveOut);
    }

    /**
     * Überprüft, ob die angegebene String tatsächlich printed wurde
     *
     * @param s String, die printed werden soll
     */
    private void shouldPrint(String s) {
        assertEquals(s, out.toString().trim());
        out.reset();
    }

    @Test
    public void soutTest() {
        System.out.println("Hi");
        shouldPrint("Hi");
        System.out.println("Bro");
        shouldPrint("Bro");
    }

    @Test
    @DisplayName("Simple multiplication should work")
    public void someTest() throws ProviderNotFoundException {
        Provider provider = new Provider();
        Provider provider1 = new Provider();
        provider.setName("O!");
        provider1.setName("Beeline");
        PrepaidSmsHandy prepaidSmsHandyTelekom = new PrepaidSmsHandy("123", provider);
        PrepaidSmsHandy prepaidSmsHandyTelekom2 = new PrepaidSmsHandy("345", provider);
        PrepaidSmsHandy prepaidSmsHandyBeeline = new PrepaidSmsHandy("222", provider1);
        TariffPlanSmsHandy tariffSmsHandyTelekom = new TariffPlanSmsHandy("001", provider);
        TariffPlanSmsHandy tariffSmsHandyBeeline = new TariffPlanSmsHandy("002", provider1);


        prepaidSmsHandyTelekom.sendSms("*101#", "");
        assertEquals(100, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom.sendSms(prepaidSmsHandyTelekom2.getNumber(), "Hallo");
        assertEquals(90, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom2.listReceived();
        prepaidSmsHandyTelekom.sendSms(prepaidSmsHandyBeeline.getNumber(), "Haaaaallo");
        assertEquals(80, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyBeeline.listReceived();

        prepaidSmsHandyTelekom.sendSmsDirect(prepaidSmsHandyTelekom2, "Direct message");
        assertEquals(80, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom.listSent();
        prepaidSmsHandyTelekom2.listReceived();
        prepaidSmsHandyTelekom.sendSmsDirect(prepaidSmsHandyBeeline, "Direct message");
        assertEquals(80, prepaidSmsHandyTelekom.getProvider().getCreditForSmsHandy(prepaidSmsHandyTelekom.getNumber()));
        prepaidSmsHandyTelekom.listSent();
        prepaidSmsHandyBeeline.listReceived();

//        tariffSmsHandyTelekom.sendSms("", "Tarrifsms");
//        String str = "Please choose a valid phone number";
//        System.out.println(str);
//        assertEquals(str, outContent.toString());
//        tariffSmsHandyTelekom.sendSms("001", "");
//        assertEquals(str, outContent.toString());
        tariffSmsHandyTelekom.sendSms("002", "Hallo aus dem anderem Seit!");
        tariffSmsHandyTelekom.listSent();
        assertEquals(99, tariffSmsHandyTelekom.getRemainingFreeSms());
        tariffSmsHandyBeeline.listReceived();
    }


}
