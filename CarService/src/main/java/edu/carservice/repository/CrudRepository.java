package edu.carservice.repository;

import java.util.List;

public interface CrudRepository<T> {
    public long count();
    public T findById(long id);
    public List<T> findAll();
    public void save(T obj);
    public void delete(T obj);
    public void deleteById(long id);
    public boolean existsById(long id);
    public void update(T obj);
}
