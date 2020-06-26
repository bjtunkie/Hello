package com.hello.lib.praksh.proto;


import com.hello.lib.praksh.exception.YetToImplementException;

import java.lang.reflect.Field;

public class ChefSerializer implements ChefTool {


    private final ChefMap cache;

    public ChefSerializer(ChefMap cache) {
        this.cache = cache;
    }

    public <T> int serialize(T input, byte[] dst, int offset) {
        Field[] fields = cache.getMapping(input.getClass());
        int mark = offset;
        offset = serialize(input, fields, dst, mark + 4);
        serInt(offset - (mark + 4), dst, mark);
        return offset;
    }

    private <T> int serialize(T input, Field[] fields, byte[] dst, int offset) {
        try {
            for (Field field : fields) {
                Class<?> k = field.getType();
                Object valueAtField = field.get(input);
                offset = serialize(valueAtField, k, dst, offset);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return offset;
    }


    private <T> int serialize(T t, Class<?> classOfT, byte[] dst, int offset) {
        final boolean isArray = classOfT.isArray();
        final Class<?> baseClass;
        if (isArray) {
            if (t == null) {
                return serInt(0, dst, offset);
            }
            baseClass = classOfT.getComponentType();
        } else {

            baseClass = classOfT;
        }


        if (int.class == baseClass) {
            if (isArray) {
                int[] input = (int[]) t;
                offset = serInt(input.length, dst, offset);
                for (int i = 0; i < input.length; i++) {
                    offset = serInt(input[i], dst, offset);
                }
            } else {
                return serInt((int) t, dst, offset);
            }

        } else if (Integer.class == baseClass) {

            if (isArray) {
                Integer[] input = (Integer[]) t;
                offset = serInt(input.length, dst, offset);

                for (int i = 0; i < input.length; i++) {
                    offset = serInt(input[i], dst, offset);
                }
            } else {
                return serInt((int) t, dst, offset);
            }
        } else if (long.class == baseClass) {

            long[] input = (long[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serLong(input[i], dst, offset);
            }

            throw new YetToImplementException();
        } else if (Long.class == baseClass) {
            Long[] input = (Long[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serLong(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (char.class == baseClass) {
            char[] input = (char[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serChar(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (Character.class == baseClass) {
            Character[] input = (Character[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serChar(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (float.class == baseClass) {
            float[] input = (float[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serFloat(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (Float.class == baseClass) {
            Float[] input = (Float[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serFloat(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (double.class == baseClass) {
            double[] input = (double[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serDouble(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (Double.class == baseClass) {
            Double[] input = (Double[]) t;
            offset = serInt(input.length, dst, offset);

            for (int i = 0; i < input.length; i++) {
                offset = serDouble(input[i], dst, offset);
            }
            throw new YetToImplementException();

        } else if (byte.class == baseClass) {
            byte[] input = (byte[]) t;
            offset = serInt(input.length, dst, offset);
            System.arraycopy(input, 0, dst, offset, input.length);
            offset += input.length;
            throw new YetToImplementException();

        } else if (Byte.class == baseClass) {
            byte[] input = (byte[]) t;
            offset = serInt(input.length, dst, offset);
            System.arraycopy(input, 0, dst, offset, input.length);
            offset += input.length;
            throw new YetToImplementException();

        } else if (boolean.class == baseClass) {
            throw new YetToImplementException();

        } else if (Boolean.class == baseClass) {
            throw new YetToImplementException();

        } else if (String.class == baseClass) {

        } else if (baseClass.isInterface()) {

        } else {

            /**
             * An object of types other than mentioned above.
             */
            Field[] fields = cache.getMapping(baseClass);
            if (isArray) {
                Object[] input = (Object[]) t;
                int sizeOfArray = input.length;
                int temp = 0;
                offset = serInt(sizeOfArray, dst, offset);
                for (int i = 0; i < sizeOfArray; i++) {
                    temp = offset + 4;
                    offset = serialize(input[i], fields, dst, temp);
                    int numberOfBytes = offset - temp;
                    serInt(numberOfBytes, dst, temp - 4);
                }

            } else {
                if (t == null) {
                    return serInt(0, dst, offset);
                }
                int temp = offset + 4;
                offset = serialize(t, fields, dst, temp);
                int numberOfBytes = offset - temp;
                serInt(numberOfBytes, dst, temp - 4);

            }

        }
        return offset;

    }

}

/**
 * if (String.class == baseClass) {
 * byte[] bytes = ((String) input).getBytes();
 * offset = serInt(bytes.length, dst, offset);
 * System.arraycopy(bytes, 0, dst, offset, bytes.length);
 * return offset + bytes.length;
 * }
 * <p>
 * <p>
 * if (String.class == baseClass) {
 * String[] input = (String[]) t;
 * offset = serInt(input.length, dst, offset);
 * for (int i = 0; i < input.length; i++) {
 * byte[] x = input[i].getBytes();
 * offset = serInt(x.length, dst, offset);
 * System.arraycopy(x, 0, dst, offset, x.length);
 * offset += x.length;
 * }
 * <p>
 * }
 */