package edu.upc.dsa.db.orm.util;


import org.apache.log4j.Logger;

import edu.upc.dsa.util.annotations.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectHelper {

    final static Logger logger = Logger.getLogger(ObjectHelper.class);

    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) continue;

            String columnName = field.getName();
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                //logger.info("Value :" + value);

                if (field.isAnnotationPresent(Column.class)) {
                    columnName = field.getAnnotation(Column.class).name();
                }
                else if (field.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    columnName = joinColumn.name();
                    //logger.info("Quo :" +columnName);

                    if (value != null) {
                        // Obtener el valor del campo referenciado dentro del objeto relacionado
                        Field referencedField = value.getClass().getDeclaredField(joinColumn.referencedColumnName());
                        referencedField.setAccessible(true);
                        value = referencedField.get(value);
                        //logger.info("NOsd " +value);
                    }
                }

                if (value != null) {
                    map.put(columnName, value);
                }

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Error al acceder a campo: " + field.getName(), e);
            }
        }

        return map;
    }


    // Recibe una clase (Class clazz), por ejemplo Partida.class.
    // Devuelve un String, que será el nombre de la columna de la clave primaria (el campo con @Id).
    public static String getIdFieldName(Class<?> clazz) {

        // Recorre todos los atributos (campos) declarados en la clase clazz, incluyendo privados.
        for (Field field : clazz.getDeclaredFields()) {
            // Si el campo tiene la anotación @Id, significa que es la clave primaria.
            if (field.isAnnotationPresent(Id.class)) {
                // Si además tiene la anotación @Column, entonces se devuelve el nombre de columna definido en @Column(name = "...").
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                return field.getName();
            }
        }
        throw new RuntimeException("No field annotated with @Id in class " + clazz.getSimpleName());
    }
}