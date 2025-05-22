package edu.upc.dsa.db.orm.util;


import org.apache.log4j.Logger;

import edu.upc.dsa.util.annotations.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectHelper {

    // String es el nombre de la columna, object es el valor del campo en el objeto
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();

        // Recorre todos los atributos de una clase de models
        // Usa reflexion (getDeclaredFields) para acceder incluso a campos private
        for (Field field : obj.getClass().getDeclaredFields()) {

            // Si el campo está anotado con @Ignore, no lo queremos persistir, así que se salta este campo.
            if (field.isAnnotationPresent(Ignore.class)) continue;

            // Por defecto, se usa el nombre del campo como nombre de columna, por si no hay anotaciones
            String columnName = field.getName();

            // Si el campo tiene la anotación @Column, sobrescribimos el nombre de columna con el valor indicado en @Column(name = "...").
            // Si no tiene @Column pero tiene @JoinColumn, usamos @JoinColumn(name = "...").
            if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).name();
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                columnName = field.getAnnotation(JoinColumn.class).name();
            }

            // Permite acceder al valor del campo aunque sea private.
            field.setAccessible(true);

            try {
                // Intenta leer el valor real de ese campo en el objeto.
                Object value = field.get(obj);

                // Solo guarda en el mapa si el valor no es null (esto evita insertar columnas con null innecesariamente,
                // aunque podrías quitar esta condición si quieres persistir también los null).
                if (value != null) {
                    map.put(columnName, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
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