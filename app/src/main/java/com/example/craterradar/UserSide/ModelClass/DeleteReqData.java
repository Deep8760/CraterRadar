package com.example.craterradar.UserSide.ModelClass;

public class DeleteReqData {
    String Pothole_Delete_Req_ID,
            Pothole_ID,
            Pothole_Old_ImageURL,
            Pothole_New_ImageURL,
            Pothole_Old_Latitude,
            Pothole_New_Latitude,
            Pothole_Old_Longotide,
            Pothole_New_Longitude,
            Pothole_Old_TimeStamp,
            Pothole_New_TimeStamp,
            Pothole_Old_Description,
            Pothole_New_Description,
            Pothole_AddedBy_UID;

 /*   public DeleteReqData(String pothole_ID, String pothole_Old_ImageURL, String pothole_New_ImageURL, String pothole_Old_Latitude, String pothole_New_Latitude, String pothole_Old_Longotide, String pothole_New_Longitude, String pothole_Old_TimeStamp, String pothole_New_TimeStamp, String pothole_Old_Description, String pothole_New_Description, String pothole_AddedBy_UID) {
        Pothole_ID = pothole_ID;
        Pothole_Old_ImageURL = pothole_Old_ImageURL;
        Pothole_New_ImageURL = pothole_New_ImageURL;
        Pothole_Old_Latitude = pothole_Old_Latitude;
        Pothole_New_Latitude = pothole_New_Latitude;
        Pothole_Old_Longotide = pothole_Old_Longotide;
        Pothole_New_Longitude = pothole_New_Longitude;
        Pothole_Old_TimeStamp = pothole_Old_TimeStamp;
        Pothole_New_TimeStamp = pothole_New_TimeStamp;
        Pothole_Old_Description = pothole_Old_Description;
        Pothole_New_Description = pothole_New_Description;
        Pothole_AddedBy_UID = pothole_AddedBy_UID;
    }*/

    public DeleteReqData(String deleteReqID, String pothole_id, String pothole_old_imageURL, String pothole_new_imageURL, String pothole_old_latitude, String pothole_new_latitude, String pothole_old_longotide, String pothole_new_longitude, String pothole_old_timeStamp, String pothole_new_timeStamp, String pothole_old_description, String pothole_new_description, String pothole_addedBy_uid) {
        Pothole_Delete_Req_ID = deleteReqID;
        Pothole_ID = pothole_id;
        Pothole_Old_ImageURL = pothole_old_imageURL;
        Pothole_New_ImageURL = pothole_new_imageURL;
        Pothole_Old_Latitude = pothole_old_latitude;
        Pothole_New_Latitude = pothole_new_latitude;
        Pothole_Old_Longotide = pothole_old_longotide;
        Pothole_New_Longitude = pothole_new_longitude;
        Pothole_Old_TimeStamp = pothole_old_timeStamp;
        Pothole_New_TimeStamp = pothole_new_timeStamp;
        Pothole_Old_Description = pothole_old_description;
        Pothole_New_Description = pothole_new_description;
        Pothole_AddedBy_UID = pothole_addedBy_uid;
    }

    public String getPothole_Delete_Req_ID() {
        return Pothole_Delete_Req_ID;
    }

    public String getPothole_ID() {
        return Pothole_ID;
    }

    public String getPothole_Old_ImageURL() {
        return Pothole_Old_ImageURL;
    }

    public String getPothole_New_ImageURL() {
        return Pothole_New_ImageURL;
    }

    public String getPothole_Old_Latitude() {
        return Pothole_Old_Latitude;
    }

    public String getPothole_New_Latitude() {
        return Pothole_New_Latitude;
    }

    public String getPothole_Old_Longotide() {
        return Pothole_Old_Longotide;
    }

    public String getPothole_New_Longitude() {
        return Pothole_New_Longitude;
    }

    public String getPothole_Old_TimeStamp() {
        return Pothole_Old_TimeStamp;
    }

    public String getPothole_New_TimeStamp() {
        return Pothole_New_TimeStamp;
    }

    public String getPothole_Old_Description() {
        return Pothole_Old_Description;
    }

    public String getPothole_New_Description() {
        return Pothole_New_Description;
    }

    public String getPothole_AddedBy_UID() {
        return Pothole_AddedBy_UID;
    }

}
