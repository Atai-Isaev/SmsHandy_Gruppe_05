package test;

import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;
import main.smsHandy.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderTest {

    private static Provider provider;
    private static Provider provider2;
    private static SmsHandy prepaid;
    private static SmsHandy tariffPlan;
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void initialize() throws ProviderNotFoundException {
        System.setOut(new PrintStream(outContent));
       provider = new Provider();
       provider.setName("Telecom");
       provider2 = new Provider();
       provider2.setName("Vodafone");
       prepaid = new PrepaidSmsHandy("015257000263",provider);
       tariffPlan = new TariffPlanSmsHandy("015250007245",provider);
    }

    @AfterAll
    static void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Test for providerList on the added provider")
    public void providerListTest(){
        assertEquals(1,Provider.providersList.size());
        assertEquals(provider, Provider.providersList.get(0));
    }

    @Test
    @DisplayName("Test for all small Methods like register(), deposit(), " +
            "getCreditForSmsHandy() in Provider class")
    public void allSmallMethodsTest(){
        assertEquals(100,provider.getCredits().get(prepaid.getNumber()));
        assertEquals(prepaid,provider.getSubscriber().get(prepaid.getNumber()));
        provider.deposit(prepaid.getNumber(),30);
        assertEquals(130,provider.getCreditForSmsHandy(prepaid.getNumber()));
        assertEquals(tariffPlan,provider.getSubscriber().get(tariffPlan.getNumber()));
        assertNull( provider.getCredits().get(tariffPlan.getNumber()));
    }

    @Test
    @DisplayName("Test for send() Method in Provider class")
    public void sendMethodTest() throws ProviderNotFoundException {
        SmsHandy anotherProviderHandy = new PrepaidSmsHandy("015258009089", provider2);

        Message message1 = new Message("Guten Tag!",new Date(),prepaid.getNumber(),tariffPlan.getNumber());
        assertTrue(provider.send(message1));
        tariffPlan.listReceived();
        assertEquals("\r\nGuten Tag!\r\n",outContent.toString()); //Ueberprueft System.out.print

        Message message2 = new Message("Mein Deposit?",new Date(),prepaid.getNumber(),"*101#");
        assertTrue(provider.send(message2));
        prepaid.listReceived();
        assertEquals("\r\n" + "Guten Tag!\r\n\r\nIhre Guthaben: 90\r\n",outContent.toString());

        Message messageWithWrongReciever = new Message("Falsche Reciever", new Date(),prepaid.getNumber(),"wrong number");
        assertFalse(provider.send(messageWithWrongReciever));

        Message messageWithWrongSender = new Message("Falsche Sender", new Date(),"wrong number",prepaid.getNumber());
        assertFalse(provider.send(messageWithWrongSender));
    }
}
