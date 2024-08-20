package uz.pdp.service;

import java.io.IOException;
import java.util.List;

public interface BaseService <T>{


    void save(T entity) throws IOException;
    void update(T entity);
    void delete(int entity);
    T getById(int id);
    List<T> getAll();

}
