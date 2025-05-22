package edu.upc.dsa.db.orm;

import edu.upc.dsa.db.orm.util.InsertQuery;
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
        InsertQuery insertQuery = QueryHelper.createInsertQuery(entity.getClass());
        logger.info("InsertQuery SQL: " + insertQuery.sql);
        logger.info("InsertQuery columns: " + insertQuery.columns);

        try (PreparedStatement pstm = conn.prepareStatement(insertQuery.sql)) {

            Map<String, Object> columnValues = ObjectHelper.objectToMap(entity);
            logger.info("MAP " + columnValues);

            int i = 1;
            for (String column : insertQuery.columns) {  // Orden garantizado
                pstm.setObject(i++, columnValues.get(column));
            }
            logger.info("Sesion " + pstm);
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

                // Por cada campo, rellenamos el objeto con los datos de la BD
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
                    field.set(entity, value);
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
        String query = QueryHelper.createSelectFindAll(theClass);
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

                    Object value = rs.getObject(columnName);
                    field.setAccessible(true);
                    field.set(entity, value);
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
        String query = QueryHelper.createSelectFindAllWithParams(theClass, params);
        List<Object> results = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            int i = 1;
            if (params != null) {
                for (Object value : params.values()) {
                    pstm.setObject(i++, value);
                }
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
                    field.set(entity, value);
                }

                results.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public void update(Object object) {
        String updateQuery = QueryHelper.createUpdateQuery(object.getClass());

        try (PreparedStatement pstm = conn.prepareStatement(updateQuery)) {

            Map<String, Object> columnValues = ObjectHelper.objectToMap(object);

            // Obtenemos el nombre de la columna id para el WHERE
            String idColumn = ObjectHelper.getIdFieldName(object.getClass());
            Object idValue = null;

            // Guardamos el valor del id para el parámetro final
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    idValue = field.get(object);
                    break;
                }
            }

            // Seteamos parámetros para SET (excluyendo el id)
            int i = 1;
            for (String col : columnValues.keySet()) {
                if (!col.equals(idColumn)) {
                    pstm.setObject(i++, columnValues.get(col));
                }
            }

            // Parámetro WHERE id = ?
            pstm.setObject(i, idValue);

            pstm.executeUpdate();

        } catch (Exception e) {
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