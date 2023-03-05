package ws.askin.files.exception;

public class NullFieldException extends RuntimeException {
    public NullFieldException(String fieldName) {
        super(String.format("Field: %s can not be null!", fieldName));
    }

    public NullFieldException() {
        super("Field can not be null!");
    }
}
