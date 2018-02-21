package com.softwarearchitecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SinkFilter extends FilterTemplate {

    private File file = new File("OutputA.dat");
    private Calendar timeStamp = Calendar.getInstance();
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

    private String stringFormat = "%-20s %-20s %-20s %n";

    private int nFrame = 0;
    private double temperature, altitude;

    public void run() {
        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(String.format(stringFormat, "Time:", "Temperature(C):", "Altitude(m):"));
            printWriter.flush();

            System.out.print("\n" + this.getName() + "::Sink Reading");

            while (true) {
                try {
                    readNextPairValues();

                    if (id == Utils.TIME_ID) {
                        timeStamp.setTimeInMillis(measurement);
                        nFrame++;
                    } else if (id == Utils.TEMPERATURE_ID) {
                        temperature = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.ALTITUDE_ID) {
                        altitude = Double.longBitsToDouble(measurement);
                    }

                    if (id == Utils.TEMPERATURE_ID) {
                        System.out.println("\n" + this.getName() + "::Sink Writing" + "\n");
                        printWriter.write(String.format(stringFormat,
                                timeStampFormat.format(timeStamp.getTime()), temperature, altitude));
                        printWriter.flush();
                    }
                } catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead);
                    break;
                }
            }

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
