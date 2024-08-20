package uz.pdp.daos;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component

public interface BaseDao<T> {
    void save(T entity) throws IOException;
    void update(T entity);
    void delete(int entity);
    T getById(int id);
    List<T> getAll();
}
