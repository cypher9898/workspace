package com.example.myapplication2.ClassObject;

import java.io.Serializable;

public class ObjectReport implements Serializable {


    private String Description;
    private String reportType;
    private String UserID;
    private String GeneratedKey;



    private boolean permission;

    public ObjectReport() {
    }

    public ObjectReport(String description, String UserID) {
        this.Description = description;
        this.UserID = UserID;
    }

    public ObjectReport(ObjectReport objectReport) {
        this.Description=objectReport.Description;
        this.reportType=objectReport.reportType;
        this.UserID=objectReport.UserID;
        this.GeneratedKey=objectReport.GeneratedKey;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    public String getGeneratedKey() {
        return GeneratedKey;
    }
    public void setGeneratedKey(String key) {
        this.GeneratedKey=key;
    }
}