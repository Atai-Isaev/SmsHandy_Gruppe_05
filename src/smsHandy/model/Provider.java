package smsHandy.model;

import java.util.Map;

/**
 * Klasse Provider.
 */
public class Provider {
    private Map<String,Integer> credits;

    /**
     * Konstruktor fuer Objekte der Klasse Provider
     */
    public Provider() {
    }

    /**
     * Sendet die SMS an den Empfaenger, wenn dieser bekannt ist.
     * @param message - die zu sendente SMS
     * @return true, wenn SMS gesendet werden konnte
     */
    public boolean send(Message message){
        return true;
    }

    /**
     * Registriert ein neues Handy bei diesem Provider.
     * @param smsHandy - das neue Handy
     */
    public void register(SmsHandy smsHandy){
    }

    /**
     * Laedt Guthaben für ein Handy auf. Das ist noetig, weil das Handy sein Guthaben nicht selbst aendern kann,
     * sondern nur der Provider. Negative Geldmengen werden hier erlaubt, um ueber diese Funktion auch die Kosten
     * fuer eine Nachricht abziehen zu koennen. Negative Werte beim haendischen Aufladen werden in der Klasse
     * SmsHandy verhindert.
     * @param number - Nummer des Telefons
     * @param amount - Hoehe des Geldbetrages
     */
    public void deposit(String number, int amount){

    }

    /**
     * Gibt das aktuelle Guthaben des betreffenden Handys zurück
     * @param number - Nummer des gewuenschten Handys
     * @return aktuelles Guthaben des Handys
     */
    public int getCreditForSmsHandy(String number){
        return 0;
    }
}
