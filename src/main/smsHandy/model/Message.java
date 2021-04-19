package main.smsHandy.model;

import java.util.Date;

/**
 * Klasse Message. Eine Nachricht, die über die SMSHandys verschickt werden kann.
 */
public class Message {
    private String content;
    private Date date;
    private String from;
    private String to;

    /**
     * Konstruktor ohne Parameter
     */
    public Message() {
    }

    /**
     * Konstruktor mit Parametern
     *
     * @param content Inhalt der Nachricht
     * @param date    Datum
     * @param from    Absender
     * @param to      Empfaenger
     */
    public Message(String content, Date date, String from, String to) {
        this.content = content;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    /**
     * Gibt die vollstaendige Nachricht als String zurueck.
     *
     * @return formatierter String, mit allen Daten
     */
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", date=" + date +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    /**
     * Gibt den Inhalt der Nachricht zurueck.
     *
     * @return aktueller Inhalt der SMS
     */
    public String getContent() {
        return content;
    }

    /**
     * Setzt den Inhalt der Nachricht.
     *
     * @param content neuer Inhalt fuer die SMS
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gibt das Erstellungsdatum der Nachricht zurück.
     *
     * @return Erstellungsdatum der SMS
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setzt das Erstellungsdatum der SMS.
     *
     * @param date Neues Datum fuer die SMS
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gibt den Absender der Nachricht zurueck.
     *
     * @return aktueller Absender der SMS
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setzt den Absender der Nachricht.
     *
     * @param from neuer Absender fuer die SMS
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gibt den Empfaenger zurueck.
     *
     * @return aktueller Empfaenger fuer die SMS
     */
    public String getTo() {
        return to;
    }

    /**
     * Setzt den Empfaenger der Nachricht.
     *
     * @param to neuer Empfaenger fuer die SMS
     */
    public void setTo(String to) {
        this.to = to;
    }
}
