package edu.upc.dsa.db.orm.util;


import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ObjectHelper {
    // Este archivo literalmente son los getters y los setters de los modelos
    // Pero para usar con la base de datos

    final static Logger logger = Logger.getLogger(ObjectHelper.class);

    // Este es para obtener los nombres de los atributos del objeto
    public static String[] getFields(Object entity) {

        Class theClass = entity.getClass();

        Field[] fields = theClass.getDeclaredFields();

        String[] sFields = new String[fields.length];
        int i=0;

        for (Field f: fields) sFields[i++]=f.getName();

        return sFields;

    }


    //property es el atributo del objeto
    public static void setter(Object object, String property, Object value) {
        try {
            // Obtenemos el tipo de atributo, ej string nombre
            Field field = object.getClass().getDeclaredField(property);
            Class fieldType = field.getType();

            // Construimos el setter, ej setNombre
            String methodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
            // Obtenemos el setter
            Method method = object.getClass().getMethod(methodName, fieldType);
            // Ejecutamos el setter
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //property es el atributo del objeto
    public static Object getter(Object object, String property) {
        try {
            // Construimos el getter, ej getNombre
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
            // Obtenemos el getter
            Method method = object.getClass().getMethod(methodName);
            // Ejecutamos el getter
            return method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
