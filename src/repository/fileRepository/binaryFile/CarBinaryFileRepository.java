package repository.fileRepository.binaryFile;

import domain.Car;
import repository.fileRepository.FileRepository;
import utils.exceptions.FileNotValidException;

import java.io.*;
import java.util.HashMap;

public class CarBinaryFileRepository extends FileRepository<Integer, Car> {

    public CarBinaryFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void readFromFile() {
        try (FileInputStream fis = new FileInputStream(this.fileName); ObjectInputStream ois = new ObjectInputStream(fis))  {
            elements = (HashMap<Integer, Car>) ois.readObject();
        } catch (IOException e) {
            // thrown by new ObjectInputStream()
            throw new FileNotValidException(e);
        } catch (ClassNotFoundException e) {
            // thrown by new FileInputStream()
            throw new FileNotValidException(e);
        }
    }

    @Override
    protected void writeToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.fileName)) ) {
            oos.writeObject(elements);
        } catch (IOException e) {
            // thrown by new ObjectInputStream()
            throw new FileNotValidException(e);
        }
    }
}
