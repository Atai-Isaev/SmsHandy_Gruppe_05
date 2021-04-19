package smsHandy.model;

/**
 * Klasse TariffPlanSmsHandy. Ein Vertragshandy, das über eine
 * bestimmte Menge an Frei-SMS verfügt. In einer späteren Version
 * könnten diese nach einer bestimmten Zeit wieder zurückgesetzt
 * werden. Dies wird vorerst noch nicht berücksichtigt.
 *
 * @version 1.0
 * @author Bekzhan SATAROV
 */
public class TariffPlanSmsHandy extends SmsHandy {

    private int remainingFreeSms = 0;

    /**
     * Konstruktor zum Erstellen eines neuen TariffPlanHandy.
     *
     * @param number die Handynummer
     * @param provider die Providerinstanz
     */
    public TariffPlanSmsHandy(String number, Provider provider) {
        super(number, provider);
    }

    /**
     * Prüft, ob Frei-SMS noch zum Senden ausreichen.
     *
     * @return noch Frei-SMS vorhanden?
     */
    @Override
    public boolean canSendSms() {
        return getRemainingFreeSms() > 0;
    }

    /**
     * Reduziert die Frei-SMS.
     */
    @Override
    public void payForSms() {
        if (canSendSms()) this.remainingFreeSms--;
    }

    /**
     * Liefert Anzahl der verbliebenen Frei-SMS.
     *
     * @return Anzahl der Frei-SMS
     */
    public int getRemainingFreeSms() {
        return remainingFreeSms;
    }
}
