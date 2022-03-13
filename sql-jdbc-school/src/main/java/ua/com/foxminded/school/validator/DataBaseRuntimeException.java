package ua.com.foxminded.school.validator;

public class DataBaseRuntimeException extends RuntimeException{
    
    public DataBaseRuntimeException() {
        super();
    }
    
    public DataBaseRuntimeException(String message) {
        super(message);
    }
    
    public DataBaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DataBaseRuntimeException(Throwable cause) {
        super(cause);
    }
}
