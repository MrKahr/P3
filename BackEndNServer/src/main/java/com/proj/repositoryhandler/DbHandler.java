package com.proj.repositoryhandler;
import java.util.Optional; 

public abstract class DbHandler<T> {
    public abstract void save(T elem);
    public abstract void saveAll(Iterable<T> iter);
    public abstract T findById(Integer id);
    public abstract boolean existsById(Integer id);
    public abstract Iterable <T> findAll();    
    public abstract Iterable <T> findAllById(Iterable <Integer> ids);
    public abstract long count();
    public abstract void deleteAllById(Iterable<Integer> userIdIterable);
    public abstract void delete(T elem);
    public abstract void deleteAll(Iterable<T> iterable);
}
