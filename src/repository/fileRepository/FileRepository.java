package repository.fileRepository;

import domain.Identifiable;
import repository.MemoryRepository;
import utils.exceptions.IDNotValidException;

import java.util.Optional;

public abstract class FileRepository<ID,T extends Identifiable<ID>> extends MemoryRepository<ID,T> {

    protected String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
        readFromFile();
    }

    protected abstract void readFromFile();

    protected abstract void writeToFile();

    @Override
    public void add(ID id, T t) throws IDNotValidException {
        super.add(id, t);
        writeToFile();
    }

    @Override
    public Optional<T> delete(ID id) throws IDNotValidException {
        Optional<T> removed = super.delete(id);
        writeToFile();
        return removed;
    }

    @Override
    public void modify(ID id, T t) throws IDNotValidException {
        super.modify(id, t);
        writeToFile();
    }
}
