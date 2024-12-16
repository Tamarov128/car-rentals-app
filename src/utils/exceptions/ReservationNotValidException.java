package utils.exceptions;

public class ReservationNotValidException extends Exception {

    public ReservationNotValidException(String message) {
        super("Reservation not valid: " + message);
    }
}
