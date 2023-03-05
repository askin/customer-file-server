package ws.askin.files.exception;

public class UserIsNotFoundException extends RuntimeException {
    public UserIsNotFoundException(String msg) {
        super(msg);
    }

    public UserIsNotFoundException(Long userId) {
        super(String.format("User: %d Not Found!", userId));
    }

    public UserIsNotFoundException() {
        super("User Not Found!");
    }
}
