package com.softwarearchitecture;

public class TemperatureFilter extends FilterTemplate {

    private double fahrenheitToCelsius(double temperature) {
        return ((((temperature) - 32) * 5) / 9);
    }

    public void run() {
        System.out.print("\n" + this.getName() + "::TemperatureFilter Reading");

        while (true) {
            try {
                readNextPairValues();

                if (id == Utils.TEMPERATURE_ID) {
                    double temperature = Double.longBitsToDouble(measurement);
                    measurement = Double.doubleToLongBits(fahrenheitToCelsius(temperature));
                }

                writePairValuesToStream();
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::TemperatureFilter Exiting; bytes read: " + bytesRead + " bytes written: " + bytesWritten);
                break;
            }
        }
    }
}
