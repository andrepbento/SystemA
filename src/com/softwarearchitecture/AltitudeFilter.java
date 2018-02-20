package com.softwarearchitecture;

public class AltitudeFilter extends FilterTemplate {

    private double feetToMeters(double feet) {
        return (feet * 0.3048);
    }

    public void run() {
        System.out.print("\n" + this.getName() + "::AltitudeFilter Reading");

        while (true) {
            try {
                readNextPairValues();

                if (id == Utils.ALTITUDE_ID) {
                    double altitude = Double.longBitsToDouble(measurement);
                    measurement = Double.doubleToLongBits(feetToMeters(altitude));
                }

                writePairValuesToStream();
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::AltitudeFilter Exiting; bytes read: " + bytesRead
                        + " bytes written: " + bytesWritten);
                break;
            }
        }
    }
}
