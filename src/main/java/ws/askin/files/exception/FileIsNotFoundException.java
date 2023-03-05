package ws.askin.files.exception;

public class FileIsNotFoundException extends RuntimeException {
    public FileIsNotFoundException(String msg) {
        super(msg);
    }

    public FileIsNotFoundException(Long fileId) {
        super(String.format("File: %d Not Found!", fileId));
    }

    public FileIsNotFoundException() {
        super("File Is Not Found!");
    }
}
