package com.hello.lib.praksh.proto;

public interface ChefTool {

    default int writeNull(byte[] dst, int offset) {
        return serInt(0, dst, offset);

    }

    default boolean isZero(byte[] src, int offset) {
        return (src[offset] == 0
                && src[offset + 1] == 0
                && src[offset + 2] == 0
                && src[offset + 3] == 0
        );
    }

    default int serInt(int input, byte[] dst, int offset) {
        for (int i = 3; i >= 0; i--) {
            dst[i + offset] = (byte) (input & 0xFF);
            input >>= 8;
        }
        return offset + Integer.BYTES;
    }

    default int serLong(long input, byte[] dst, int offset) {
        for (int i = 7; i >= 0; i--) {
            dst[i + offset] = (byte) (input & 0xFF);
            input >>= 8;
        }
        return offset + Long.BYTES;
    }

    default int serChar(char x, byte[] dst, int offset) {
        dst[offset] = (byte) ((x >> 8) & 0xFF);
        dst[offset + 1] = (byte) (x & 0xFF);
        return offset + Character.BYTES;
    }

    default int serFloat(float input, byte[] dst, int offset) {
        int x = Float.floatToRawIntBits(input);
        for (int i = 3; i >= 0; i--) {
            dst[i + offset] = (byte) (x & 0xFF);
            x >>= 8;
        }
        return offset + Float.BYTES;
    }

    default int serDouble(double input, byte[] dst, int offset) {
        long x = Double.doubleToRawLongBits(input);
        for (int i = 7; i >= 0; i--) {
            dst[i + offset] = (byte) (x & 0xFF);
            x >>= 8;
        }
        return offset + Double.BYTES;
    }

    default int serShort(short x, byte[] dst, int offset) {
        dst[offset] = (byte) ((x >> 8) & 0xff);
        dst[offset + 1] = (byte) (x & 0xff);
        return offset + Short.BYTES;
    }

}
