package utils.validators;

import domain.Identifiable;

public interface Validator<ID, T extends Identifiable<ID>> {

    boolean isValid(T identifiable);
}
