package com.svitlasystem.database;


public class DBContract {

    private DBContract() {
        // hide
    }

    public interface Table {
        String BEER = "beer";
        String LOCATION = "location";
    }

    public interface Beer {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
    }

    public interface Location {
        String ID = "id";
        String NAME = "name";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }
}
