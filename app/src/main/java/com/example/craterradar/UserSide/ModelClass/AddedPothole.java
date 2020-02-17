package com.example.craterradar.UserSide.ModelClass;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class AddedPothole {
    String dangerlevel,timestamp,description;
    LatLng location;

    public AddedPothole() {
    }

    public AddedPothole(LatLng location, String dangerlevel, String timestamp, String description) {
        this.location = location;
        this.dangerlevel = dangerlevel;
        this.timestamp = timestamp;
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getDangerlevel() {
        return dangerlevel;
    }

    public void setDangerlevel(String dangerlevel) {
        this.dangerlevel = dangerlevel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
