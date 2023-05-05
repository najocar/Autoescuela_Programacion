package com.najocar.autoescuela.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> extends AutoCloseable{
    List<T> findAll() throws SQLException ;
    T findById(String id) throws SQLException ;
    T save(T entity) throws SQLException ;
    T delete(T entity) throws SQLException ;
}
