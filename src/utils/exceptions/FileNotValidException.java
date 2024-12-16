package utils.exceptions;

public class FileNotValidException extends RuntimeException {

    public FileNotValidException(String message) {
        super("File not valid: " + message);
    }
    public FileNotValidException(Exception e) {
        super("File not valid: " + e.getMessage());
    }
}
