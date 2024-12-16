package utils.exceptions;

public class CarNotValidException extends Exception {

    public CarNotValidException(String message) {
        super("Car not valid: " + message);
    }
}
