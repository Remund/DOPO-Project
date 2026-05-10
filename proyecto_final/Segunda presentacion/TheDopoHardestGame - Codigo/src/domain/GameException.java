package domain;

public class GameException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private int errorCode;

    public GameException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}