package ws.askin.files.exception;

public class UserIsNotAuthorizedException extends RuntimeException {
    public UserIsNotAuthorizedException(String msg) {
        super(msg);
    }

    public UserIsNotAuthorizedException(Long userId) {
        super(String.format("User: %d is not authorized!", userId));
    }

    public UserIsNotAuthorizedException() {
        super("User is not authorized!");
    }
}
