package com.softwarearchitecture;

import java.nio.ByteBuffer;

public class Utils {

    static final int ID_LENGTH = 4;
    static final int MEASUREMENT_LENGTH = 8;

    static final int TIME_ID = 0;
    static final int SPEED_ID = 1;
    static final int ALTITUDE_ID = 2;
    static final int PRESSURE_ID = 3;
    static final int TEMPERATURE_ID = 4;
    static final int PITCH_ID = 5;

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[Integer.BYTES]);
        buffer.putInt(x);
        return buffer.array();
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[Long.BYTES]);
        buffer.putLong(x);
        return buffer.array();
    }
}
