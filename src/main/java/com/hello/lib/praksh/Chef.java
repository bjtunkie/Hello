package com.hello.lib.praksh;

import com.hello.lib.praksh.proto.ChefDeSerializer;
import com.hello.lib.praksh.proto.ChefMap;
import com.hello.lib.praksh.proto.ChefSerializer;

public abstract class Chef {
    protected final byte[] bytes = new byte[1024 * 1024 * 8]; // 8 mb of data
    protected static final int defaultOffset = 6;

    protected final ChefSerializer serializer;
    protected final ChefDeSerializer deSerializer;

    Chef() {
        final ChefMap cache = new ChefMap();
        serializer = new ChefSerializer(cache);
        deSerializer = new ChefDeSerializer(cache);
    }

    public abstract <T> int serialize(T input, byte[] output);

    public abstract <T> byte[] serialize(T input);


    public abstract <T> T deSerialize(byte[] array, Class<T> clazz);


}