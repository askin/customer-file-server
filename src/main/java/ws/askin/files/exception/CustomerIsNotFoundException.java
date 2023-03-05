package ws.askin.files.exception;

public class CustomerIsNotFoundException extends Exception {
    public CustomerIsNotFoundException(String msg) {
        super(msg);
    }

    public CustomerIsNotFoundException(Long customerId) {
        super(String.format("Customer: %d Not Found!", customerId));
    }

    public CustomerIsNotFoundException() {
        super("Customer Is Not Found!");
    }
}
