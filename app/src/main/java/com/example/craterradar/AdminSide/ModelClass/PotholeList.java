package com.example.craterradar.AdminSide.ModelClass;

public class PotholeList {
    String potholeID,potholeImageurl,pothole_lat,pothole_long,potholeDangerLevel,potholeDescription,potholeTimeStamp,potholeUploadedby;

    public PotholeList(String potholeID, String potholeImageurl, String pothole_lat, String pothole_long, String potholeDangerLevel, String potholeDescription, String potholeTimeStamp, String potholeUploadedby) {
        this.potholeID = potholeID;
        this.potholeImageurl = potholeImageurl;
        this.pothole_lat = pothole_lat;
        this.pothole_long = pothole_long;
        this.potholeDangerLevel = potholeDangerLevel;
        this.potholeDescription = potholeDescription;
        this.potholeTimeStamp = potholeTimeStamp;
        this.potholeUploadedby = potholeUploadedby;
    }

    public String getPotholeID() {
        return potholeID;
    }

    public String getPotholeImageurl() {
        return potholeImageurl;
    }

    public String getPothole_lat() {
        return pothole_lat;
    }

    public String getPothole_long() {
        return pothole_long;
    }

    public String getPotholeDangerLevel() {
        return potholeDangerLevel;
    }

    public String getPotholeDescription() {
        return potholeDescription;
    }

    public String getPotholeTimeStamp() {
        return potholeTimeStamp;
    }

    public String getPotholeUploadedby() {
        return potholeUploadedby;
    }
}
