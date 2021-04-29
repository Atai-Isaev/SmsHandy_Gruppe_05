package main.smsHandy.model;

import main.smsHandy.exception.ProviderNotFoundException;

/**
 * Klasse TariffPlanSmsHandy. Ein Vertragshandy, das über eine
 * bestimmte Menge an Frei-SMS verfügt. In einer späteren Version
 * könnten diese nach einer bestimmten Zeit wieder zurückgesetzt
 * werden. Dies wird vorerst noch nicht berücksichtigt.
 *
 * @author Bekzhan SATAROV
 * @version 1.0
 */
public class TariffPlanSmsHandy extends SmsHandy {

    private int remainingFreeSms;

    /**
     * Konstruktor zum Erstellen eines neuen TariffPlanHandy.
     *
     * @param number   die Handynummer
     * @param provider die Providerinstanz
     */
    public TariffPlanSmsHandy(String number, Provider provider) throws ProviderNotFoundException {
        super(number, provider);
        this.remainingFreeSms = 100;
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

    /**
     * Schickt eine SMS ueber den Provider an den Empfaenger.
     *
     * @param to      - der Empfaenger der SMS
     * @param content - der Inhalt der SMS
     */
    @Override
    public void sendSms(String to, String content) throws ProviderNotFoundException {
        super.sendSms(to, content);
        payForSms();
    }
}
