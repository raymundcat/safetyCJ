package com.example.raymundcat.safetycj.models;

import java.util.ArrayList;

/**
 * Created by Raymund on 21/05/2016.
 */
public class EventLocations {
    public ArrayList<EventLocation> locations;

    public class EventLocation{
        public double lat;
        public double lng;
        public double timestamp;
    }
}
