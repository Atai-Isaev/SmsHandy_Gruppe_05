package main.smsHandy.exception;

public class InvalidNumberException extends RuntimeException{
    public InvalidNumberException(String message) {
        super(message);
    }
}
