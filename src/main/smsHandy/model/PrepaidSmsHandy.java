package main.smsHandy.model;

import main.smsHandy.exception.ProviderNotFoundException;

/**
 * Klasse PrepaidSmsHandy. Ein Handy, das über ein beim
 * Provider verwaltetes Guthaben verfügt und dessen
 * SMS-Versand über dieses Guthaben abgerechnet wird.
 */
public class PrepaidSmsHandy extends SmsHandy {

    private static final int COST_PER_SMS = 10;

    /**
     * Konstruktor zum Erstellen eines neuen PrepaidHandy.
     *
     * @param number   die Handynummer
     * @param provider die Providerinstanz
     */
    public PrepaidSmsHandy(String number, Provider provider) throws ProviderNotFoundException {
        super(number, provider);
        try {
            this.deposit(100);
        } catch (NullPointerException e) {
            throw new ProviderNotFoundException("Provider can't be null");
        }
    }

    /**
     * Prüft, ob das Guthaben noch für das Versenden einer SMS reicht.
     *
     * @return ist das Guthaben noch ausreichend?
     */
    @Override
    public boolean canSendSms() throws ProviderNotFoundException {
        try {
            return this.getProvider().getCreditForSmsHandy(this.getNumber()) >= 10;
        } catch (NullPointerException e) {
            throw new ProviderNotFoundException("Provider can't be null");
        }
    }

    /**
     * Zieht die Kosten für eine SMS vom Guhaben ab.
     */
    @Override
    public void payForSms() throws ProviderNotFoundException {
        if (this.canSendSms()) this.deposit(-COST_PER_SMS);
    }

    /**
     * Lädt das Guthaben fuer das SmsHandy-Objekt auf.
     *
     * @param amount Menge, um die Aufgeladen werden soll
     */
    public void deposit(int amount) throws ProviderNotFoundException {
        try {
            if (amount != 0) this.getProvider().deposit(this.getNumber(), amount);
        } catch (NullPointerException e) {
            throw new ProviderNotFoundException("Provider can't be null");
        }
    }
}
