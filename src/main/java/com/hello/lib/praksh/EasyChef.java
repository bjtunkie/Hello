package com.hello.lib.praksh;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EasyChef extends Chef {
    private final Gson gson;

    public EasyChef() {
        super();
        gson = new GsonBuilder().create();
    }

    @Override
    public <T> int serialize(T input, byte[] output) throws RuntimeException {
        String x = gson.toJson(input);
        byte[] bytes = x.getBytes();

        int length = bytes.length + defaultOffset;

        if (length > output.length) {
            throw new RuntimeException("the output byte array is of length less than the overall size.");
        }
        System.arraycopy(bytes, 0, output, defaultOffset, bytes.length);
        return length;

    }

    @Override
    public <T> byte[] serialize(T input) {
        String x = gson.toJson(input);
        byte[] bytes = x.getBytes();

        int size = bytes.length + defaultOffset;
        byte[] array = new byte[size];
        System.arraycopy(bytes, 0, array, defaultOffset, bytes.length);
        return array;

    }

    @Override
    public <T> T deSerialize(byte[] array, Class<T> clazz) {
        String json = new String(array, defaultOffset, array.length - defaultOffset);
        return gson.fromJson(json, clazz);
    }


}
