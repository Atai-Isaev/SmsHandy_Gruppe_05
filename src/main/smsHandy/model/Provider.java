package main.smsHandy.model;

import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasse Provider.
 */
public class Provider {
    private String name;
    private final Map<String, Integer> credits;

    private final Map<String, SmsHandy> subscriber;

    public static final ArrayList<Provider> providersList = new ArrayList<>();

    /**
     * Konstruktor fuer Objekte der Klasse Provider
     */
    public Provider() {
        this.credits = new HashMap<>();
        this.subscriber = new HashMap<>();
        providersList.add(this);
    }

    /**
     * Sendet die SMS an den Empfaenger, wenn dieser bekannt ist.
     *
     * @param message - die zu sendente SMS
     * @return true, wenn SMS gesendet werden konnte
     */
    public boolean send(Message message) throws ProviderNotFoundException {
        Provider receiverProvider = findProviderFor(message.getTo());
        Provider senderProvider = findProviderFor(message.getFrom());
            //can not be null
            if (senderProvider != null && senderProvider.equals(this)){
                SmsHandy senderSmsHandyInThisProvider = subscriber.get(message.getFrom());
                SmsHandy receiverSmsHandyInThisProvider = subscriber.get(message.getTo());

                if (message.getTo().equals("*101#")) {
                    Message tempMessage = new Message();
                    tempMessage.setContent("Ihre Guthaben: " + getCreditForSmsHandy(message.getFrom()));
                    tempMessage.setFrom(this.getName());
                    //TODO format of Date
                    tempMessage.setDate(new Date());
                    tempMessage.setTo(message.getFrom());
                    senderSmsHandyInThisProvider.receiveSms(tempMessage);
                    return true;
                } else if (receiverSmsHandyInThisProvider != null) {
                    if (senderSmsHandyInThisProvider.canSendSms()) {
                        receiverSmsHandyInThisProvider.receiveSms(message);
                        senderSmsHandyInThisProvider.payForSms();
                        return true;
                    } else {
                        return false;
                    }
                } else if (receiverProvider != null) {
                    receiverSmsHandyInThisProvider = receiverProvider.getSubscriber().get(message.getTo());
                    receiverSmsHandyInThisProvider.receiveSms(message);
                    senderSmsHandyInThisProvider.payForSms();
                    return true;
                }
                return false;
            }
            else if (receiverProvider!=null){
                receiverProvider.send(message);
            }
            return false;

    }

    /**
     * Registriert ein neues Handy bei diesem Provider.
     *
     * @param smsHandy - das neue Handy
     */
    public void register(SmsHandy smsHandy) throws ProviderNotFoundException {
        try {
            if (!subscriber.containsKey(smsHandy.getNumber())){
                if (smsHandy.getClass() == PrepaidSmsHandy.class && !credits.containsKey(smsHandy.getNumber())){
                    credits.put(smsHandy.getNumber(), 0);
                    subscriber.put(smsHandy.getNumber(), smsHandy);
                }
                else if (smsHandy.getClass() == TariffPlanSmsHandy.class) {
                    subscriber.put(smsHandy.getNumber(), smsHandy);
                }
            }
            else {
                System.out.println("Diese Nummer ist bereits beim Provider "+this.getName()+" registriert ");
            }
        }catch (NullPointerException e){
            throw new ProviderNotFoundException("Provider can't be null");
        }

    }

    /**
     * Laedt Guthaben für ein Handy auf. Das ist noetig, weil das Handy sein Guthaben nicht selbst aendern kann,
     * sondern nur der Provider. Negative Geldmengen werden hier erlaubt, um ueber diese Funktion auch die Kosten
     * fuer eine Nachricht abziehen zu koennen. Negative Werte beim haendischen Aufladen werden in der Klasse
     * SmsHandy verhindert.
     *
     * @param number - Nummer des Telefons
     * @param amount - Hoehe des Geldbetrages
     */
    public void deposit(String number, int amount) {
        try {
            if(credits.containsKey(number)){
                credits.put(number, credits.get(number) + amount);
            }
            else{
                System.out.println("Diese Nummer ist nicht Prepaid");
            }
        } catch (NullPointerException e) {
            throw new SmsHandyNotFoundException("Number can't be empty");
        }

    }

    /**
     * Gibt das aktuelle Guthaben des betreffenden Handys zurück
     *
     * @param number - Nummer des gewuenschten Handys
     * @return aktuelles Guthaben des Handys
     */
    public int getCreditForSmsHandy(String number) {
        try {
            return credits.get(number);
        } catch (NullPointerException e) {
            throw new SmsHandyNotFoundException("Number can't be empty");
        }
    }

    /**
     * Überprüft, ob die Nummer in der Map beim Provider steht
     *
     * @param receiver - Nummer des Telefons, des wir ausprobieren
     * @return liefert genau dann true zurück,
     * wenn ein Teilnehmer mit der Rufnummer receiver bei diesem Provider registriert ist.
     */
    private boolean canSendTo(String receiver) {
        try {
            return subscriber.containsKey(receiver);
        }catch (NullPointerException e){
            throw new SmsHandyNotFoundException("Number can't be empty");
        }
    }

    /**
     * liefert den Provider zurück, bei dem der Teilnehmer mit der
     * Rufnummer receiver registriert ist, oder null, wenn es die Nummer nicht gibt.
     *
     * @param receiver - Nummer des Telefons, des wir ausprobieren
     * @return Provider oder null
     */
    private static Provider findProviderFor(String receiver) throws SmsHandyNotFoundException{
        for (Provider p : providersList) {
            if (p.canSendTo(receiver))
                return p;
        }
        return null;
    }

    /**
     * Gibt den aktuellen Name zurück.
     *
     * @return Name den Provider
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Name.
     *
     * @param name - Name den Provider
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den liste mit Nummers zurück.
     *
     * @return Map = Map mit Prepaid Nummers
     */
    public Map<String, Integer> getCredits() {
        return credits;
    }

    /**
     * Gibt den liste mit Nummers zurück.
     *
     * @return Map = Map mit alle Nummers von Provider
     */
    public Map<String, SmsHandy> getSubscriber() {
        return subscriber;
    }
}
