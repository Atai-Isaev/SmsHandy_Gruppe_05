package main.smsHandy.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.smsHandy.exception.InvalidNumberException;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstrakte Basisklasse SmsHandy.
 */

public abstract class SmsHandy {
    private String number;
    private Provider provider;
    private ObservableList<Message> sent;
    private ObservableList<Message> received;

    /**
     * Konstruktor fuer Objekte der Klasse SmsHandy
     *
     * @param number   - die Handynummer
     * @param provider - die Providerinstanz
     */
    public SmsHandy(String number, Provider provider) throws ProviderNotFoundException {
        try {
            this.number = number;
            this.provider = provider;
            this.sent = FXCollections.observableArrayList();
            this.received = FXCollections.observableArrayList();
            this.getProvider().register(this);
        } catch (NullPointerException e) {
            throw new ProviderNotFoundException("Provider can't be null");
        }


    }

    /**
     * Schickt eine SMS ueber den Provider an den Empfaenger.
     *
     * @param to      - der Empfaenger der SMS
     * @param content - der Inhalt der SMS
     */
    public void sendSms(String to, String content) throws ProviderNotFoundException {
        if (!to.equals(this.getNumber())) {
            Message message = new Message();
            message.setContent(content);
            message.setDate(new Date());
            message.setFrom(this.getNumber());
            message.setTo(to);
            if (provider.send(message))
                this.sent.add(message);
            else
                throw new InvalidNumberException("Please choose a valid phone number!");
        } else
            throw new InvalidNumberException("You can not send sms to yourself!");
    }

    /**
     * Abstrakte Methode zur Prüfung, ob der Versand der SMS noch bezahlt werden kann.
     *
     * @return ist der Versand der SMS noch möglich?
     */
    public abstract boolean canSendSms() throws ProviderNotFoundException;


    /**
     * Abstrakte Methode zum Bezahlen des SMS-Versand.
     */
    public abstract void payForSms() throws ProviderNotFoundException;


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
            this.sent.add(message);
            peer.receiveSms(message);
        } catch (NullPointerException e) {
            throw new SmsHandyNotFoundException("SmsHandy can't be null");
        }

    }

    /**
     * Empfaengt eine SMS und speichert diese in den empfangenen SMS
     *
     * @param message - das Message-Objekt, welches an das zweite Handy gesendet werden soll
     */
    public void receiveSms(Message message) {

        this.received.add(message);
    }

    /**
     * Gibt eine Liste aller gesendete SMS auf der Konsole aus.
     */
    public void listSent() {
        System.out.println();
        this.sent.forEach(message -> System.out.println(message.getContent()));
    }

    /**
     * Gibt eine Liste aller empfangenen SMS auf der Konsole aus.
     */
    public void listReceived() {
        System.out.println();
        this.received.forEach(message -> System.out.println(message.getContent()));
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

    public ObservableList<Message> getSent() {
        return sent;
    }

    public void setSent(ObservableList<Message> sent) {
        this.sent = sent;
    }

    public ObservableList<Message> getReceived() {
        return received;
    }

    public void setReceived(ObservableList<Message> received) {
        this.received = received;
    }
}
