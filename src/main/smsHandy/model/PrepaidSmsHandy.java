package main.smsHandy.model;

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
     * @param number die Handynummer
     * @param provider die Providerinstanz
     */
    public PrepaidSmsHandy(String number, Provider provider) {
        super(number, provider);
        provider.register(this);
        this.deposit(100);
        System.out.println("Created new PrepaidSmsHandy: "+number);
    }

    /**
     * Prüft, ob das Guthaben noch für das Versenden einer SMS reicht.
     *
     * @return ist das Guthaben noch ausreichend?
     */
    @Override
    public boolean canSendSms() {
        return this.getProvider().getCreditForSmsHandy(this.getNumber()) >= 10;
    }

    /**
     * Zieht die Kosten für eine SMS vom Guhaben ab.
     */
    @Override
    public void payForSms() {
        if (this.canSendSms()) this.deposit(-COST_PER_SMS);
        else System.out.println("Method payForSms: your balance ="+this.getProvider().getCreditForSmsHandy(this.getNumber()));
    }

    /**
     * Lädt das Guthaben fuer das SmsHandy-Objekt auf.
     *
     * @param amount Menge, um die Aufgeladen werden soll
     */
    public void deposit(int amount) {
        if (amount!=0){
            this.getProvider().deposit(this.getNumber(), amount);
            System.out.println("Method deposit(PrepSmsHandy): +"+amount+". Now your credit is "+this.getProvider().getCreditForSmsHandy(this.getNumber()));
        }
        else System.out.println("Method deposit(PrepSmsHandy): amount is equals 0");

    }
}
