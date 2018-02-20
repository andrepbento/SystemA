package com.softwarearchitecture;

public class Plumber {

    public static void main(String argv[]) {

        SourceFilter sourceFilter = new SourceFilter("FlightData.dat");
        TemperatureFilter temperatureFilter = new TemperatureFilter();
        AltitudeFilter altitudeFilter = new AltitudeFilter();
        SinkFilter sinkFilter = new SinkFilter();

        sinkFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(sourceFilter);

        sourceFilter.start();
        temperatureFilter.start();
        altitudeFilter.start();
        sinkFilter.start();
    }
}
