package com.example.craterradar.UserSide.ModelClass;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class AddedPothole {
    String latitude,longitude,dangerlevel,timestamp,description;

    public AddedPothole() {
    }

    public AddedPothole(String latitude,String longitude, String dangerlevel, String timestamp, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dangerlevel = dangerlevel;
        this.timestamp = timestamp;
        this.description = description;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
