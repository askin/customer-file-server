package ws.askin.files.exception;

public class EmailIsAlreadyTakenException extends RuntimeException {
    public EmailIsAlreadyTakenException(String email) {
        super(String.format("Email: %s is already taken!", email));
    }

    public EmailIsAlreadyTakenException() {
        super("Email is already taken!");
    }
}
