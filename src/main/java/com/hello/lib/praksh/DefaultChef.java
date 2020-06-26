package com.hello.lib.praksh;

public class DefaultChef extends Chef {
    public DefaultChef() {
        super();
    }

    @Override
    public <T> int serialize(T input, byte[] output) {
        return 0;
    }

    @Override
    public <T> byte[] serialize(T input) {
        return new byte[0];
    }

    @Override
    public <T> T deSerialize(byte[] array, Class<T> clazz) {
        return null;
    }
    /****
     public <T> int serialize(T input, byte[] output, int offset) {
     int count = serializer.serialize(input, bytes, offset);
     System.arraycopy(bytes, 0, output, 0, count);
     return count;
     }

     public <T> byte[] serialize(T input, int offset) {
     int count = serializer.serialize(input, bytes, offset);
     byte[] array = new byte[count];
     System.arraycopy(bytes, 0, array, 0, count);
     return array;
     }

     public <T> T deSerialize(byte[] array, Class<T> clazz, int offset) {
     return deSerialize(array, clazz, offset, array.length - offset);
     }

     public <T> T deSerialize(byte[] array, Class<T> clazz, int offset, int length) {
     ByteBuffer buffer = ByteBuffer.wrap(array, offset, length);
     return (T) deSerializer.deSerialize(ByteBuffer.wrap(array), clazz);
     }
     ***/
}
