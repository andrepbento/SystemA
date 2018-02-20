package com.softwarearchitecture;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class SourceFilter extends FilterTemplate {

    private String fileName = null;

    public SourceFilter(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        int bytesRead = 0;
        int bytesWritten = 0;
        DataInputStream in = null;

        byte dataByte;

        try {
            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file...");

            while (true) {
                dataByte = in.readByte();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            }
        } catch (EOFException eofException) {
            System.out.println("\n" + this.getName() + "::End of file reached...");
            try {
                in.close();
                ClosePorts();
                System.out.println("\n" + this.getName() + "::Read file complete, bytes read::" + bytesRead
                        + " bytes written: " + bytesWritten);

            } catch (Exception closeErr) {
                System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeErr);
            }
        } catch (IOException iox) {
            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        }
    }
}
