package model;

/**
 * Wird eingesetzt um beim Abfangen von Exceptions beim Erstellen des Protokolls
 * die ProtocolWriteException mit einer entsprechenden Meldung zu werfen.
 *
 *
 * Created by Nett on 03.01.2017.
 * @author Nett
 */
public class ProtocolWriteException extends RuntimeException{

    public ProtocolWriteException(String message) {
        super(message);
    }
}

