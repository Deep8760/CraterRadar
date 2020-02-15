package com.example.craterradar;

public class Potholes {
    String potholeid,potholeImageURL,location_Lat,location_Long,dangerLevel,timeStamp,description;

    public Potholes() {
    }

    public Potholes(String potholeid, String potholeImageURL, String location_Lat, String location_Long, String dangerLevel, String timeStamp, String description) {
        this.potholeid = potholeid;
        this.potholeImageURL = potholeImageURL;
        this.location_Lat = location_Lat;
        this.location_Long = location_Long;
        this.dangerLevel = dangerLevel;
        this.timeStamp = timeStamp;
        this.description = description;
    }

    public String getPotholeid() {
        return potholeid;
    }

    public void setPotholeid(String potholeid) {
        this.potholeid = potholeid;
    }

    public String getPotholeImageURL() {
        return potholeImageURL;
    }

    public void setPotholeImageURL(String potholeImageURL) {
        this.potholeImageURL = potholeImageURL;
    }

    public String getLocation_Lat() {
        return location_Lat;
    }

    public void setLocation_Lat(String location_Lat) {
        this.location_Lat = location_Lat;
    }

    public String getLocation_Long() {
        return location_Long;
    }

    public void setLocation_Long(String location_Long) {
        this.location_Long = location_Long;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
