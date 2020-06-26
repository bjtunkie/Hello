package com.hello.lib.praksh.proto;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class ChefDeSerializer implements ChefTool {
    private final ChefMap cache;

    public ChefDeSerializer(ChefMap cache) {
        this.cache = cache;
    }

    public <T> T deSerialize(ByteBuffer buf, Field[] fields, Class<T> clazz) {

        T instance = null;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> type = field.getType();
            Object valueAtField = deSerializeObject(buf, type);

            try {
                field.set(instance, valueAtField);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public <M> M deSerialize(ByteBuffer buf, Class<M> k) {

        Field[] fields = cache.getMapping(k);
        int size = buf.getInt();
        if (size == 0) return null;
        return deSerialize(buf, fields, k);
    }

    private <M> Object deSerializeObject(ByteBuffer buf, Class<M> k) {

        final Class<?> baseClass;
        if (k.isArray()) {

            int lengthOfArray = buf.getInt();
            if (lengthOfArray == 0) {
                return null;
            }

            baseClass = k.getComponentType();


            if (int.class == baseClass) {

                int[] x = new int[lengthOfArray];
                for (int i = 0; i < lengthOfArray; i++) {
                    x[i] = buf.getInt();
                }

                return x;
            } else if (Integer.class == baseClass) {
                Integer[] x = new Integer[lengthOfArray];
                for (int i = 0; i < lengthOfArray; i++) {
                    x[i] = buf.getInt();
                }
                return x;

            } else {
                Field[] fields = cache.getMapping(baseClass);
                M[] instance = (M[]) Array.newInstance(baseClass, lengthOfArray);
                for (int i = 0; i < lengthOfArray; i++) {
                    instance[i] = (M) deSerialize(buf, fields, baseClass);
                }

                return instance;
            }
        } else {
            baseClass = k;

            if (int.class == baseClass || Integer.class == baseClass) {
                return buf.getInt();
            } else if (long.class == baseClass || Long.class == baseClass) {
                return buf.getLong();
            } else if (char.class == baseClass || Character.class == baseClass) {
                return buf.getChar();
            } else if (float.class == baseClass || Float.class == baseClass) {
                return buf.getFloat();
            } else if (double.class == baseClass || Double.class == baseClass) {
                return buf.getDouble();
            } else if (short.class == baseClass || Short.class == baseClass) {
                return buf.getShort();
            } else if (byte.class == baseClass || Byte.class == baseClass) {
                return buf.get();
            } else if (boolean.class == baseClass || Boolean.class == baseClass) {
                return buf.get() > 0;
            } else {
                int sizeOfObject = buf.getInt();
                if (sizeOfObject == 0) {
                    // null object
                    return null;
                }
                Field[] fields = cache.getMapping(baseClass);
                return deSerialize(buf, fields, baseClass);

            }
        }

        /*
        else if (long.class == baseClass) {
            long[] x = new long[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getLong();
            }
            return x;

        } else if (Long.class == baseClass) {
            Long[] x = new Long[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getLong();
            }
            return x;

        } else if (char.class == baseClass) {
            char[] x = new char[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getChar();
            }
            return x;

        } else if (Character.class == baseClass) {
            Character[] x = new Character[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getChar();
            }
            return x;

        } else if (float.class == baseClass) {
            float[] x = new float[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getFloat();
            }
            return x;

        } else if (Float.class == baseClass) {
            Float[] x = new Float[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getFloat();
            }
            return x;

        } else if (double.class == baseClass) {
            double[] x = new double[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getDouble();
            }
            return x;

        } else if (Double.class == baseClass) {
            Double[] x = new Double[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                x[i] = buf.getDouble();
            }
            return x;

        } else if (String.class == baseClass) {
            String[] x = new String[lengthOfArray];
            for (int i = 0; i < lengthOfArray; i++) {
                int numberOfBytes = buf.getInt();
                int pos = buf.position();
                x[i] = new String(buf.array(), pos, numberOfBytes);
                buf.position(pos + numberOfBytes);
            }
            return x;

        }

        */

    }


}
