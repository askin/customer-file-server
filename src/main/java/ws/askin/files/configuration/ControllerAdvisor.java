package ws.askin.files.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;

class CustomErrorMessage {
    private String message;
    private HttpStatus status;
    CustomErrorMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}

@ControllerAdvice
public class ControllerAdvisor {
    @ResponseBody
    @ExceptionHandler(UserIsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorMessage userNotFoundExceptionHandler(UserIsNotFoundException ex) {
        return new CustomErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(UserIsNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CustomErrorMessage userIsNotAuthorizedExceptionHandler(UserIsNotAuthorizedException ex) {
        return new CustomErrorMessage(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
