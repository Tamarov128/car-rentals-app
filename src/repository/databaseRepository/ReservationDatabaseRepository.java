package repository.databaseRepository;

import domain.Car;
import domain.Reservation;
import utils.exceptions.IDNotValidException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReservationDatabaseRepository extends DatabaseRepository<Integer, Reservation> {

    public ReservationDatabaseRepository(String path) {
        this.JDBC_URL = "jdbc:sqlite:C:/Users/Elena/GitHub/Advanced Programming Methods/Assignments/Assignment 8/" + path;
        openConnection();
        loadFromDatabase();
        closeConnection();
    }

    @Override
    public void loadFromDatabase() {
        // load cars from database into memory
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Reservation");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int carID = rs.getInt("carID");
                // find car from Car database
                PreparedStatement psCar = conn.prepareStatement("SELECT * FROM Car WHERE id = ?");
                psCar.setInt(1, carID);
                ResultSet rsCar = psCar.executeQuery();
                // load car information
                rsCar.next();
                String make = rsCar.getString("make");
                String model = rsCar.getString("model");
                float rentalPrice = rsCar.getFloat("rentalPrice");
                int manufacturingYear = rsCar.getInt("manufacturingYear");
                boolean available = rsCar.getInt("available") == 1;
                Car car = new Car(carID, make, model, rentalPrice, manufacturingYear);
                car.setAvailable(available);
                // construct reservation
                Reservation reservation = new Reservation(id, name, car);
                elements.put(reservation.getId(), reservation);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Integer id, Reservation reservation) throws IDNotValidException {
        // add reservation to memory
        super.add(id, reservation);
        // add reservation to database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Reservation VALUES (?, ?, ?)");
            ps.setInt(1, reservation.getId());
            ps.setString(2, reservation.getName());
            ps.setInt(3, reservation.getRentedCar().getId());
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Reservation> delete(Integer id) throws IDNotValidException {
        // delete reservation from memory
        Optional<Reservation> deleted = super.delete(id);
        // delete reservation from database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Reservation WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }

    @Override
    public void modify(Integer id, Reservation reservation) throws IDNotValidException {
        // modify reservation in memory
        super.modify(id, reservation);
        // modify reservation in database
        try {
            openConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE Reservation SET name = ?, carID = ? WHERE id = ?");
            ps.setString(1, reservation.getName());
            ps.setInt(2, reservation.getRentedCar().getId());
            ps.setInt(3, reservation.getId());
            ps.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
