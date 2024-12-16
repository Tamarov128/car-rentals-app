package domain;

import java.io.Serializable;
import java.util.Objects;

public class Car implements Identifiable<Integer>, Serializable {

    private int id;
    private String make;
    private String model;
    private float rentalPrice;
    private int manufacturingYear;
    private boolean available;

    public Car() {}

    public Car(int id, String make, String model, float rentalPrice, int manufacturingYear) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.rentalPrice = rentalPrice;
        this.manufacturingYear = manufacturingYear;
        // upon creation, the car is available
        this.available = true;
    }

    public Car(Car car) {
        // copy constructor
        this.id = car.getId();
        this.make = car.getMake();
        this.model = car.getModel();
        this.rentalPrice = car.getRentalPrice();
        this.manufacturingYear = car.getManufacturingYear();
        this.available = car.isAvailable();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(float rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        // cars are identified by ID
        return id == car.id;
    }

    @Override
    public int hashCode() {
        // cars are identified by ID
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", manufacturingYear=" + manufacturingYear +
                ", available=" + available +
                '}';
    }
}
