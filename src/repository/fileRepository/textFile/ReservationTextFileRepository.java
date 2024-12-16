package repository.fileRepository.textFile;

import domain.Car;
import domain.Reservation;
import repository.fileRepository.FileRepository;
import utils.exceptions.FileNotValidException;
import utils.exceptions.IDNotValidException;

import java.io.*;

public class ReservationTextFileRepository extends FileRepository<Integer, Reservation> {

    public ReservationTextFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while((line = br.readLine()) != null) {
                // inputted non-empty line
                String[] tokens = line.split(",");
                if(tokens.length != 8){
                    throw new FileNotValidException("Incorrect number of line arguments!");
                }
                else{
                    try {
                        Car car = new Car(Integer.parseInt(tokens[2]), tokens[3], tokens[4], Float.parseFloat(tokens[5]), Integer.parseInt(tokens[6]));
                        car.setAvailable(Boolean.parseBoolean(tokens[7]));
                        Reservation reservation = new Reservation(Integer.parseInt(tokens[0]), tokens[1], car);
                        add(reservation.getId(), reservation);
                    }
                    catch (NumberFormatException e) {
                        throw new FileNotValidException("Incorrect line argument types!");
                    }
                    catch (IDNotValidException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            //thrown by new FileReader()
            throw new FileNotValidException(e);
        }
        catch (IOException e) {
            //thrown by readLine()
            throw new FileNotValidException(e);
        }
    }

    @Override
    protected void writeToFile() {
        // example reservation:
        // 1,elena,2,Mazda,MX-30,250.0,2020,false
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Reservation reservation : getAll()) {
                Car car = reservation.getRentedCar();
                bw.write(reservation.getId() + ","
                        + reservation.getName() + ","
                        + car.getId() + ","
                        + car.getMake() + ","
                        + car.getModel() + ","
                        + car.getRentalPrice() + ","
                        + car.getManufacturingYear() + ","
                        + car.isAvailable() + "\n");
            }
        } catch (IOException e) {
            //thrown by new FileReader()
            throw new FileNotValidException(e);
        }
    }
}
