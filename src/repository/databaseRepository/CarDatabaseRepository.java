package repository.databaseRepository;

import domain.Car;
import utils.exceptions.IDNotValidException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarDatabaseRepository extends DatabaseRepository<Integer, Car> {

    public CarDatabaseRepository(String path) {
        this.JDBC_URL = "jdbc:sqlite:C:/Users/Elena/GitHub/Advanced Programming Methods/Assignments/Assignment 8/" + path;
        openConnection();
        loadFromDatabase();
        closeConnection();
    }

    @Override
    public void loadFromDatabase() {
        // load cars from database into memory
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Car");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String make = rs.getString("make");
                String model = rs.getString("model");
                float rentalPrice = rs.getFloat("rentalPrice");
                int manufacturingYear = rs.getInt("manufacturingYear");
                boolean available = rs.getInt("available") == 1;
                Car car = new Car(id, make, model, rentalPrice, manufacturingYear);
                car.setAvailable(available);
                elements.put(car.getId(), car);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Integer id, Car car) throws IDNotValidException {
        // add car to memory
        super.add(id, car);
        // add car to database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Car VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, car.getId());
            ps.setString(2, car.getMake());
            ps.setString(3, car.getModel());
            ps.setFloat(4, car.getRentalPrice());
            ps.setInt(5, car.getManufacturingYear());
            ps.setInt(6, car.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Car> delete(Integer id) throws IDNotValidException {
        // delete car from memory
        Optional<Car> deleted = super.delete(id);
        // delete car from database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Car WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }

    @Override
    public void modify(Integer id, Car car) throws IDNotValidException {
        // modify car in memory
        super.modify(id, car);
        // modify car in database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE Car SET make = ?, model = ?, rentalPrice = ?, manufacturingYear = ?, available = ? WHERE id = ?");
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setFloat(3, car.getRentalPrice());
            ps.setInt(4, car.getManufacturingYear());
            ps.setInt(5, car.isAvailable() ? 1 : 0);
            ps.setInt(6, id);
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
