package ws.askin.files.exception;

public class UserNameIsAlreadyTakenException extends RuntimeException {
    public UserNameIsAlreadyTakenException(String userName) {
        super(String.format("UserName: %s is already taken!", userName));
    }

    public UserNameIsAlreadyTakenException() {
        super("UserName is already taken!");
    }
}
