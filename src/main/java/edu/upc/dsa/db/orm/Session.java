package edu.upc.dsa.db.orm;

import java.util.HashMap;
import java.util.List;

public interface Session {
    void save(Object entity);                                           // Crud
    Object get(Class theClass, Object ID);
    List<Object> findAll(Class theClass);                               // cR
    List<Object> findAll(Class theClass, HashMap<String, Object> params);// cRud
    void update(Object object);                                         // crUd
    void delete(Object object);                                         // cruD
    void close();
}