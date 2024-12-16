package repository;

import domain.Identifiable;
import utils.exceptions.IDNotValidException;

import java.util.Optional;

public interface Repository<ID, T extends Identifiable<ID>> {

    void add(ID id, T t) throws IDNotValidException;

    Optional<T> delete(ID id) throws IDNotValidException;

    void modify(ID id, T t) throws IDNotValidException;

    Optional<T> findById(ID id);

    Iterable<T> getAll();

}
