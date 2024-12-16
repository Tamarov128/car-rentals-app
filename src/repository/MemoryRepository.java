package repository;

import domain.Identifiable;
import utils.exceptions.IDNotValidException;

import java.util.HashMap;
import java.util.Optional;

public class MemoryRepository<ID,T extends Identifiable<ID>> implements Repository<ID,T> {

    protected HashMap<ID,T> elements;

    public MemoryRepository() {
        elements = new HashMap<>();
    }

    @Override
    public void add(ID id, T t) throws IDNotValidException {
        if (elements.containsKey(id)) {
            throw new IDNotValidException("Element with given ID already exists in the repository");
        }
        elements.put(id, t);
    }

    @Override
    public Optional<T> delete(ID id) throws IDNotValidException {
        if (!elements.containsKey(id)) {
            throw new IDNotValidException("Element with given ID does not exist in the repository");
        }
        T removed = elements.remove(id);
        return Optional.ofNullable(removed);
    }

    @Override
    public void modify(ID id, T t) throws IDNotValidException {
        if (!elements.containsKey(id)) {
            throw new IDNotValidException("Element with given ID does not exist in the repository");
        }
        if (!t.getId().equals(id)) {
            throw new IDNotValidException("The ID given does not correspond to that of the new element");
        }
        elements.put(id, t);
    }

    @Override
    public Optional<T> findById(ID id) {
        T found = elements.get(id);
        return Optional.ofNullable(found);
    }

    @Override
    public Iterable<T> getAll() {
        return elements.values();
    }

}
