package dao;

import java.util.List;
import java.util.Map;

public interface IDao<ID extends java.io.Serializable, T> {
    void add(ID id, T item);
    void update(ID id, T entity);
    void delete(ID id);
    Map<ID, T> getAll();
    T get(ID id);
}

