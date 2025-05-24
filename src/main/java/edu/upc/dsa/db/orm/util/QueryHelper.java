package edu.upc.dsa.db.orm.util;

import edu.upc.dsa.util.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class QueryHelper {

    // Devuelve el nombre de la tabla como un String.
    public static String getTableName(Class<?> clazz) {

        // Se verifica si la clase tiene la anotación @Table.
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).name();
        }
        // Si la anotación no está presente, se devuelve el nombre de la clase en minúsculas como nombre de la tabla por defecto.
        return clazz.getSimpleName().toLowerCase();
    }

    // Recibe como parámetro la clase del modelo (clazz) de la que se quiere generar la consulta SQL de inserción.
    public static PreparedQuery createInsertQuery(Class<?> clazz) {
        String table = getTableName(clazz);

        // columns guardará los nombres de las columnas.
        StringJoiner columnsJoiner = new StringJoiner(", ");

        // placeholders tendrá los signos de interrogación (?)
        StringJoiner placeholders = new StringJoiner(", ");

        List<String> columnsList = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) continue;

            String columnName = field.getName();

            if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).name();
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                columnName = field.getAnnotation(JoinColumn.class).name();
            }

            columnsJoiner.add(columnName);
            placeholders.add("?");
            columnsList.add(columnName);
        }

        // Ejemplo de salida:
        //"INSERT INTO partida (id_partida, id_usuario, vidas, monedas) VALUES (?, ?, ?, ?)"
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnsJoiner, placeholders);
        return new PreparedQuery(sql, columnsList);
    }

    public static String createSelectByIdQuery(Class<?> clazz) {
        String table = getTableName(clazz);
        String idColumn = ObjectHelper.getIdFieldName(clazz);

        // SELECT * FROM partida WHERE id_partida = ?
        return String.format("SELECT * FROM %s WHERE %s = ?", table, idColumn);
    }

    // Devuelve query para obtener todas las filas sin filtro
    public static PreparedQuery createSelectFindAll(Class<?> clazz) {
        String table = getTableName(clazz);
        String sql = "SELECT * FROM " + table;
        return new PreparedQuery(sql, new ArrayList<>());  // lista vacía y mutable
    }

    // Devuelve query para obtener filas con filtro WHERE con parámetros
    public static PreparedQuery createSelectFindAllWithParams(Class<?> clazz, Map<String, Object> params) {
        String table = getTableName(clazz);
        if (params == null || params.isEmpty()) {
            return createSelectFindAll(clazz);
        }

        List<String> orderedKeys = new ArrayList<>(params.keySet());  // garantiza orden
        StringJoiner whereClause = new StringJoiner(" AND ");
        for (String column : orderedKeys) {
            whereClause.add(column + " = ?");
        }

        String sql = String.format("SELECT * FROM %s WHERE %s", table, whereClause);
        return new PreparedQuery(sql, orderedKeys);
    }

    public static String createDeleteByIdQuery(Class<?> clazz) {
        String table = getTableName(clazz);
        String idColumn = ObjectHelper.getIdFieldName(clazz);
        return String.format("DELETE FROM %s WHERE %s = ?", table, idColumn);
    }

    public static PreparedQuery createUpdateQuery(Class<?> clazz) {
        String table = getTableName(clazz);
        String idColumn = ObjectHelper.getIdFieldName(clazz);

        StringJoiner setClause = new StringJoiner(", ");
        List<String> columnsList = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) continue;
            if (field.isAnnotationPresent(Id.class)) continue;

            String columnName = field.getName();

            if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).name();
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                columnName = field.getAnnotation(JoinColumn.class).name();
            }

            setClause.add(columnName + " = ?");
            columnsList.add(columnName);
        }

        String sql = String.format("UPDATE %s SET %s WHERE %s = ?", table, setClause, idColumn);
        return new PreparedQuery(sql, columnsList);
    }
}