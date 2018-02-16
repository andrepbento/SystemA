package com.softwarearchitecture;

public class TemperatureFilter extends FilterFramework {

    public double fahrenheitToCelsius(double temperature) {
        return ((((temperature) - 32) * 5) / 9);
    }

    public void run() {
        byte dataByte;                // This is the data byte read from the stream
        int bytesRead = 0;                // This is the number of bytes read from the stream
        int bytesWritten = 0;                // This is the number of bytes written from the stream

        long measurement;                // This is the word used to store all measurements - conversions are illustrated.
        int id;                            // This is the measurement id
        int i;                            // This is a loop counter

        System.out.print("\n" + this.getName() + "::TemperatureFilter Reading");

        while (true) {
            try {
                id = 0;

                for (i = 0; i < Utils.ID_LENGTH; i++) {
                    dataByte = ReadFilterInputPort();
                    id = id | (dataByte & 0xFF);

                    if (i != Utils.ID_LENGTH - 1) {
                        id = id << 8;
                    } // if

                    bytesRead++;
                } // for

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

                if (id == Utils.TEMPERATURE_ID) {
                    double temperature = Double.longBitsToDouble(measurement);
                    measurement = Double.doubleToLongBits(fahrenheitToCelsius(temperature));
                } // if

                // Write ID to Stream
                for (byte b : Utils.intToBytes(id)) {
                    WriteFilterOutputPort(b);
                    bytesWritten++;
                }

                // Write Measurement to Stream
                for (byte b : Utils.longToBytes(measurement)) {
                    WriteFilterOutputPort(b);
                    bytesWritten++;
                }


            } // try
            catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::TemperatureFilter Exiting; bytes read: " + bytesRead + " bytes written: " + bytesWritten);
                break;

            } // catch

        } // while

    } // run

} // TemperatureFilter