package smsHandy.model;

/**
 * Klasse PrepaidSmsHandy. Ein Handy, das über ein beim
 * Provider verwaltetes Guthaben verfügt und dessen
 * SMS-Versand über dieses Guthaben abgerechnet wird.
 *
 * @version 1.0
 * @author Bekzhan SATAROV
 */
public class PrepaidSmsHandy extends SmsHandy {

    private static final int COST_PER_SMS = 10;

    /**
     * Konstruktor zum Erstellen eines neuen PrepaidHandy.
     *
     * @param number die Handynummer
     * @param provider die Providerinstanz
     */
    public PrepaidSmsHandy(String number, Provider provider) {
        super(number, provider);
    }

    /**
     * Prüft, ob das Guthaben noch für das Versenden einer SMS reicht.
     *
     * @return ist das Guthaben noch ausreichend?
     */
    @Override
    public boolean canSendSms() {
        return false;
    }

    /**
     * Zieht die Kosten für eine SMS vom Guhaben ab.
     */
    @Override
    public void payForSms() {}

    /**
     * Lädt das Guthaben fuer das SmsHandy-Objekt auf.
     *
     * @param amount Menge, um die Aufgeladen werden soll
     */
    public void deposit(int amount) {}
}
