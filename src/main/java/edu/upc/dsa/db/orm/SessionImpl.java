package edu.upc.dsa.db.orm;

import edu.upc.dsa.db.orm.util.PreparedQuery;
import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.db.orm.util.QueryHelper;
import edu.upc.dsa.util.annotations.Column;
import edu.upc.dsa.util.annotations.Id;
import edu.upc.dsa.util.annotations.JoinColumn;
import org.apache.log4j.Logger;

import edu.upc.dsa.util.annotations.Ignore;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SessionImpl implements Session {

    final static Logger logger = Logger.getLogger(SessionImpl.class);

    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Object entity) {
        PreparedQuery preparedQuery = QueryHelper.createInsertQuery(entity.getClass());
        //logger.info("InsertQuery SQL: " + preparedQuery.sql);
        //logger.info("InsertQuery columns: " + preparedQuery.columns);

        try (PreparedStatement pstm = conn.prepareStatement(preparedQuery.sql)){
            Map<String, Object> columnValues = ObjectHelper.objectToMap(entity);
            //logger.info("MAP " + columnValues);

            int i = 1;
            for (String column : preparedQuery.columns) {  // Orden garantizado
                pstm.setObject(i++, columnValues.get(column));
            }
            //logger.info("Sesion " + pstm);
            pstm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(Class theClass, Object ID) {
        String selectQuery = QueryHelper.createSelectByIdQuery(theClass);

        try (PreparedStatement pstm = conn.prepareStatement(selectQuery)) {
            pstm.setObject(1, ID);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();

                for (Field field : theClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Ignore.class)) continue;

                    String columnName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).name();
                    } else if (field.isAnnotationPresent(JoinColumn.class)) {
                        columnName = field.getAnnotation(JoinColumn.class).name();
                    }

                    Object value = rs.getObject(columnName);
                    field.setAccessible(true);

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        Class<?> relatedType = field.getType();
                        Object relatedInstance = relatedType.getDeclaredConstructor().newInstance();

                        for (Field f : relatedType.getDeclaredFields()) {
                            if (f.isAnnotationPresent(Id.class)) {
                                f.setAccessible(true);
                                f.set(relatedInstance, value);
                                break;
                            }
                        }

                        field.set(entity, relatedInstance);
                    } else {
                        field.set(entity, value);
                    }
                }
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object get(Class theClass, HashMap<String, Object> params) {
        PreparedQuery queryObj = QueryHelper.createSelectFindAllWithParams(theClass, params);
        String query = queryObj.sql;
        List<String> orderedKeys = queryObj.columns;

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            int i = 1;
            for (String key : orderedKeys) {
                pstm.setObject(i++, params.get(key));
            }

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();

                for (Field field : theClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Ignore.class)) continue;

                    String columnName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).name();
                    } else if (field.isAnnotationPresent(JoinColumn.class)) {
                        columnName = field.getAnnotation(JoinColumn.class).name();
                    }

                    Object value = rs.getObject(columnName);
                    field.setAccessible(true);

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        Class<?> relatedType = field.getType();
                        Object relatedInstance = relatedType.getDeclaredConstructor().newInstance();

                        for (Field f : relatedType.getDeclaredFields()) {
                            if (f.isAnnotationPresent(Id.class)) {
                                f.setAccessible(true);
                                f.set(relatedInstance, value);
                                break;
                            }
                        }

                        field.set(entity, relatedInstance);
                    } else {
                        field.set(entity, value);
                    }
                }
                return entity;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public List<Object> findAll(Class theClass) {
        PreparedQuery queryObj = QueryHelper.createSelectFindAll(theClass);
        String query = queryObj.sql;
        List<Object> results = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();

                for (Field field : theClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Ignore.class)) continue;

                    String columnName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).name();
                    } else if (field.isAnnotationPresent(JoinColumn.class)) {
                        columnName = field.getAnnotation(JoinColumn.class).name();
                    }

                    //logger.info("columnName : " + columnName);
                    Object value = rs.getObject(columnName);
                    //logger.info("value : " + value);
                    field.setAccessible(true);

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        // Campo de tipo entidad relacionada (FK)
                        Class<?> fieldType = field.getType(); // CategoriaObjeto
                        Object joinObject = fieldType.getDeclaredConstructor().newInstance();

                        // Buscamos el campo con la anotación @Id en esa clase y le ponemos el valor
                        for (Field f : fieldType.getDeclaredFields()) {
                            if (f.isAnnotationPresent(Id.class)) {
                                f.setAccessible(true);
                                f.set(joinObject, value); // setId_categoria("1")
                                break;
                            }
                        }

                        field.set(entity, joinObject); // asignamos CategoriaObjeto con id seteado
                    } else {
                        // Campo simple
                        field.set(entity, value);
                    }

                }

                results.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public List<Object> findAll(Class theClass, HashMap<String, Object> params) {
        PreparedQuery queryObj = QueryHelper.createSelectFindAllWithParams(theClass, params);
        String query = queryObj.sql;
        List<String> orderedKeys = queryObj.columns;
        List<Object> results = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            int i = 1;
            for (String key : orderedKeys) {
                pstm.setObject(i++, params.get(key));
            }

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();

                for (Field field : theClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Ignore.class)) continue;

                    String columnName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).name();
                    } else if (field.isAnnotationPresent(JoinColumn.class)) {
                        columnName = field.getAnnotation(JoinColumn.class).name();
                    }

                    Object value = rs.getObject(columnName);
                    field.setAccessible(true);

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        Class<?> relatedType = field.getType();
                        Object relatedInstance = relatedType.getDeclaredConstructor().newInstance();

                        for (Field f : relatedType.getDeclaredFields()) {
                            if (f.isAnnotationPresent(Id.class)) {
                                f.setAccessible(true);
                                f.set(relatedInstance, value);
                                break;
                            }
                        }

                        field.set(entity, relatedInstance);
                    } else {
                        field.set(entity, value);
                    }
                }

                results.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    @Override
    public void update(Object entity) {
        PreparedQuery preparedQuery = QueryHelper.createUpdateQuery(entity.getClass());
        Map<String, Object> columnValues = ObjectHelper.objectToMap(entity);


        try (PreparedStatement pstm = conn.prepareStatement(preparedQuery.sql)) {
            int i = 1;
            for (String column : preparedQuery.columns) {
                pstm.setObject(i++, columnValues.get(column));
            }

            // Seteas el valor del ID al final
            String idColumn = ObjectHelper.getIdFieldName(entity.getClass());
            Field idField = entity.getClass().getDeclaredField(idColumn);
            idField.setAccessible(true);
            pstm.setObject(i, idField.get(entity));

            pstm.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object object) {
        String deleteQuery = QueryHelper.createDeleteByIdQuery(object.getClass());
        Object idValue = null;

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    idValue = field.get(object);
                    break;
                }
            }

            try (PreparedStatement pstm = conn.prepareStatement(deleteQuery)) {
                pstm.setObject(1, idValue);
                pstm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}