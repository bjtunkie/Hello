package com.hello.lib.praksh.proto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChefMap {
    private static final int staticOrTransientModifier = (Modifier.STATIC | Modifier.TRANSIENT);
    private final Map<Class, Field[]> listOfFields = new HashMap<>();

    private Field[] analyzeClass(Class<?> k) {

        Map<String, Field> map = new TreeMap<>();
        Class<?> clazz = k;
        while (!Object.class.equals(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null)
                for (Field field : fields) {
                    if ((field.getModifiers() & 8) != 0) {
                        /**
                         * is static... skip
                         */
                        continue;

                    }
                    field.setAccessible(true);
                    System.out.println("\tAnalyzing field: " + field.getName());
                    map.put(field.getName(), field);
                }
            clazz = clazz.getSuperclass();
        }

        return map.values().toArray(new Field[map.size()]);
    }

    public <T> Field[] getMapping(T object) {

        /**
         * No need to worry about the class types as they would be
         * channeled to the overloaded function in below
         */
        return analyzeClass(object.getClass());
    }

    public Field[] getMapping(Class<?> clazz) {

        Field[] f = listOfFields.get(clazz);
        if (f == null) {
            f = analyzeClass(clazz);
            listOfFields.put(clazz, f);
        }

//        System.out.println("Listing out the fields: " + clazz.getName());
//        for (Field field : f) {
//            System.out.println(field.getName() + " : " + field.getType());
//        }
        return f;

    }

}
