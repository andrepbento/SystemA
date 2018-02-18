package com.softwarearchitecture;

public class AltitudeFilter extends FilterTemplate {

    public double feetToMeters(double feet) {
        return (feet * 0.3048);
    }

    public void run() {
        System.out.print("\n" + this.getName() + "::TemperatureFilter Reading");

        while (true) {
            try {
                readNextId();
                readNextMeasurement();

                if (id == Utils.ALTITUDE_ID) {
                    double altitude = Double.longBitsToDouble(measurement);
                    measurement = Double.doubleToLongBits(feetToMeters(altitude));
                } // if

                writeIdToStream();
                writeMeasurementToStream();
            } // try
            catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::AltitudeFilter Exiting; bytes read: " + bytesRead
                        + " bytes written: " + bytesWritten);
                break;
            } // catch
        } // while
    } // run
} // AltitudeFilter
