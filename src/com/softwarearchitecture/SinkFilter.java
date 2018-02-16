package com.softwarearchitecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SinkFilter extends FilterFramework {

    private File file = new File("OutputA.dat");
    private Calendar timeStamp = Calendar.getInstance();
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

    String stringFormat = "%-20s %-20s %-20s %n";

    private int nFrame = 0;
    private double temperature, altitude;

    public void run() {
        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(String.format(stringFormat, "Time:", "Temperature(C):", "Altitude(m):"));
            printWriter.flush();

            byte dataByte;
            int bytesRead = 0;

            long measurement;
            int id;
            int i;

            System.out.print("\n" + this.getName() + "::Sink Reading");

            while (true) {
                try {
                    id = 0;

                    for (i = 0; i < Utils.ID_LENGTH; i++) {
                        dataByte = ReadFilterInputPort();    // This is where we read the byte from the stream...

                        id = id | (dataByte & 0xFF);        // We append the byte on to ID...

                        if (i != Utils.ID_LENGTH - 1)                // If this is not the last byte, then slide the
                        {                                    // previously appended byte to the left by one byte
                            id = id << 8;                    // to make room for the next byte we append to the ID

                        } // if

                        bytesRead++;                        // Increment the byte count

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

                    if (id == Utils.TIME_ID) {
                        timeStamp.setTimeInMillis(measurement);
                        nFrame++;
                    } else if (id == Utils.TEMPERATURE_ID) {
                        temperature = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.ALTITUDE_ID) {
                        altitude = Double.longBitsToDouble(measurement);
                    }

                    if (id == Utils.TIME_ID && nFrame > 1) {
                        System.out.println("\n" + this.getName() + "::Sink Writing" + "\n");
                        printWriter.write(String.format(stringFormat,
                                timeStampFormat.format(timeStamp.getTime()), temperature, altitude));
                        printWriter.flush();
                    }
                } // try
                catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead);
                    break;
                } // catch

            } // while

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    } // run

} // SinkFilter