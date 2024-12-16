package utils.validators;

import domain.Car;

import java.time.Year;

public class CarValidator implements Validator<Integer, Car> {

    @Override
    public boolean isValid(Car car) {
        return car.getRentalPrice() >= 0 && car.getManufacturingYear() >= 1886 && car.getManufacturingYear() <= Year.now().getValue();
    }
}
