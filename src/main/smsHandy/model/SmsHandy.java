package main.smsHandy.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstrakte Basisklasse SmsHandy.
 */

public abstract class SmsHandy {
    private String number;
    private Provider provider;
    private List<Message> sentMessages = new ArrayList<>();
    private List<Message> receivedMessages = new ArrayList<>();

    /**
     * Konstruktor fuer Objekte der Klasse SmsHandy
     *
     * @param number   - die Handynummer
     * @param provider - die Providerinstanz
     */
    public SmsHandy(String number, Provider provider) {
        this.number = number;
        this.provider = provider;
    }

    /**
     * Schickt eine SMS ueber den Provider an den Empfaenger.
     *
     * @param to      - der Empfaenger der SMS
     * @param content - der Inhalt der SMS
     */
    public void sendSms(String to, String content) {
        Message message = new Message();
        message.setContent(content);
        //TODO add format
        message.setDate(new Date());
        message.setFrom(this.getNumber());
        message.setTo(to);
        provider.send(message);
    }

    /**
     * Abstrakte Methode zur Prüfung, ob der Versand der SMS noch bezahlt werden kann.
     *
     * @return ist der Versand der SMS noch möglich?
     */
    public abstract boolean canSendSms();


    /**
     * Abstrakte Methode zum Bezahlen des SMS-Versand.
     */
    public abstract void payForSms();


    /**
     * Schickt eine SMS ohne den Provider an den Empfaenger
     *
     * @param peer-   das empfangende Handy
     * @param content - der Inhalt der SMS
     */
    public void sendSmsDirect(SmsHandy peer, String content) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setDate(new Date());
            message.setFrom(this.getNumber());
            message.setTo(peer.getNumber());
            this.sentMessages.add(message);
        } catch (NullPointerException e) {
            System.out.println("SmsHandy can't be null");
        }

    }

    /**
     * Empfaengt eine SMS und speichert diese in den empfangenen SMS
     *
     * @param message - das Message-Objekt, welches an das zweite Handy gesendet werden soll
     */
    public void receiveSms(Message message) {
        try {
            this.receivedMessages.add(message);
        } catch (NullPointerException e) {
            System.out.println("Message can't be null");
        }
    }

    /**
     * Gibt eine Liste aller gesendete SMS auf der Konsole aus.
     */
    public void listSent() {
        this.sentMessages.forEach(message -> System.out.println(message.toString()));
    }

    /**
     * Gibt eine Liste aller empfangenen SMS auf der Konsole aus.
     */
    public void listReceived() {
        this.receivedMessages.forEach(message -> System.out.println(message.toString()));
    }

    /**
     * Gibt die Handynummer zurück.
     *
     * @return die Handynummer
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gibt den aktuellen Provider zurück.
     *
     * @return aktueller Provider des Handys
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Setzt den Provider.
     *
     * @param provider - ProviderInstanz
     */
    public void setProvider(Provider provider) {
        if (this.provider != provider)
            this.provider = provider;
    }


}
