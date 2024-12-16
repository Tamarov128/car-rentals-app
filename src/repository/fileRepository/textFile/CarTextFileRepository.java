package repository.fileRepository.textFile;

import domain.Car;
import utils.exceptions.FileNotValidException;
import repository.fileRepository.FileRepository;
import utils.exceptions.IDNotValidException;

import java.io.*;

public class CarTextFileRepository extends FileRepository<Integer, Car> {

    public CarTextFileRepository(String fileName) {
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
                if(tokens.length != 6){
                    throw new FileNotValidException("Incorrect number of line arguments!");
                }
                else{
                    try {
                        Car car = new Car(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Float.parseFloat(tokens[3]), Integer.parseInt(tokens[4]));
                        car.setAvailable(Boolean.parseBoolean(tokens[5]));
                        add(car.getId(), car);
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Car car : getAll()) {
                bw.write(car.getId() + ","
                + car.getMake() + ","
                + car.getModel() + ","
                + car.getRentalPrice() + ","
                + car.getManufacturingYear() + ","
                + car.isAvailable() + '\n');
            }
        } catch (IOException e) {
            //thrown by new FileWriter()
            throw new FileNotValidException(e);
        }
    }
}
