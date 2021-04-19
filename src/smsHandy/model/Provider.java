package smsHandy.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Klasse Provider.
 */
public class Provider {
    private Map<String,Integer> credits;

    private Map<String,SmsHandy> subscriber;

    private static ArrayList<Provider> providerList;
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
        if(!credits.containsKey(smsHandy.getNumber())){
            credits.put(smsHandy.getNumber(), 0);
            subscriber.put(smsHandy.getNumber(), smsHandy);
        }
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
        try {
            if(amount>=0){
                credits.put(number,credits.get(number) + amount);
            }else {
                credits.put(number,credits.get(number) - amount);
            }
        }catch (NullPointerException e){
            System.out.println("Diese Nummer ist nicht existiert ");
        }

    }

    /**
     * Gibt das aktuelle Guthaben des betreffenden Handys zurück
     * @param number - Nummer des gewuenschten Handys
     * @return aktuelles Guthaben des Handys
     */
    public int getCreditForSmsHandy(String number){
        try {
            return credits.get(number);
        }catch (NullPointerException e){
            System.out.println("Diese Nummer ist nicht existiert ");
            return 0;
        }
    }

    /**
     *  Überprüft, ob die Nummer in der Map beim Provider steht
     * @param receiver - Nummer des Telefons, des wir ausprobieren
     * @return liefert genau dann true zurück,
     * wenn ein Teilnehmer mit der Rufnummer receiver bei diesem Provider registriert ist.
     */
    private boolean canSendTo(String receiver){
        return subscriber.containsKey(receiver) && credits.containsKey(receiver);
    }

    /**
     *  liefert den Provider zurück, bei dem der Teilnehmer mit der
     *  Rufnummer receiver registriert ist, oder null, wenn es die Nummer nicht gibt.
     * @param receiver - Nummer des Telefons, des wir ausprobieren
     * @return Provider oder null
     */
    private static Provider findProviderFor(String receiver){
        for (Provider p: providerList) {
            if(p.canSendTo(receiver))
                return p;
        }
        return null;
    }
}
