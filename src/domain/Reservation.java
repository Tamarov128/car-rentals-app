package domain;

import java.io.Serializable;
import java.util.Objects;

public class Reservation implements Identifiable<Integer>, Serializable {

    private int id;
    private String name;
    private float price;
    private Car rentedCar;

    public Reservation(int id, String name, Car rentedCar) {
        this.id = id;
        this.name = name;
        this.rentedCar = rentedCar;
        // the price is initialised with the rental price of the rented car
        this.price = rentedCar.getRentalPrice();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Car getRentedCar() {
        return rentedCar;
    }

    public void setRentedCar(Car rentedCar) {
        this.rentedCar = rentedCar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        // reservations are identified by ID
        return id == that.id;
    }

    @Override
    public int hashCode() {
        // reservations are identified by ID
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", rentedCar=" + rentedCar +
                '}';
    }
}
