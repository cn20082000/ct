package ct.rmi.base;

import ct.rmi.util.exception.DatabaseFailedException;

import java.util.List;

public interface BaseCrud<T> {
    T create(T t) throws DatabaseFailedException;
    List<T> getAll();
    T get(Long id);
    void update(T t) throws DatabaseFailedException;
    void delete(Long id) throws DatabaseFailedException;
}
