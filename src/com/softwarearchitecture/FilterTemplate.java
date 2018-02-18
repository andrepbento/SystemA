package com.softwarearchitecture;

abstract public class FilterTemplate extends FilterFramework {

    byte dataByte;                // This is the data byte read from the stream
    int bytesRead = 0;                // This is the number of bytes read from the stream
    int bytesWritten = 0;                // This is the number of bytes written from the stream

    long measurement;                // This is the word used to store all measurements - conversions are illustrated.
    int id;                            // This is the measurement id
    int i;                            // This is a loop counter

    public void readNextId() throws EndOfStreamException {
        id = 0;

        for (i = 0; i < Utils.ID_LENGTH; i++) {
            dataByte = ReadFilterInputPort();
            id = id | (dataByte & 0xFF);

            if (i != Utils.ID_LENGTH - 1) {
                id = id << 8;
            } // if

            bytesRead++;
        } // for
    }

    public void readNextMeasurement() throws EndOfStreamException {
        measurement = 0;

        for (i = 0; i < Utils.MEASUREMENT_LENGTH; i++) {
            dataByte = ReadFilterInputPort();
            measurement = measurement | (dataByte & 0xFF);    // We append the byte on to measurement...

            if (i != Utils.MEASUREMENT_LENGTH - 1)                    // If this is not the last byte, then slide the
            {                                                // previously appended byte to the left by one byte
                measurement = measurement << 8;                // to make room for the next byte we append to the
                // measurement
            } // if

            bytesRead++;                                    // Increment the byte count

        } // if
    }

    public void writeIdToStream() {
        for (byte b : Utils.intToBytes(id)) {
            WriteFilterOutputPort(b);
            bytesWritten++;
        }
    }

    public void writeMeasurementToStream() {
        for (byte b : Utils.longToBytes(measurement)) {
            WriteFilterOutputPort(b);
            bytesWritten++;
        }
    }

    public abstract void run();
}
