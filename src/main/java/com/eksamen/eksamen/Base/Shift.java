package com.eksamen.eksamen.Base;

public class Shift {
    private int id;
    private String comment;
    private int approved;
    private String start_date;
    private String start_time;
    private String end_date;
    private String end_time;
    private String location;
    private int staffId;
    private String staffName;

    public Shift(int id, String comment, int approved, String start_date, String start_time, String end_date, String end_time, String location, int staffId, String staffName) {
        this.id = id;
        this.comment = comment;
        this.approved = approved;
        this.start_date = start_date;
        this.start_time = start_time.substring(0,5);
        this.end_date = end_date;
        this.end_time = end_time.substring(0,5);
        this.location = location;
        this.staffId = staffId;
        this.staffName = staffName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", approved=" + approved +
                ", start_date='" + start_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_date='" + end_date + '\'' +
                ", end_time='" + end_time + '\'' +
                ", location='" + location + '\'' +
                ", staffId=" + staffId +
                ", staffName='" + staffName + '\'' +
                '}';
    }
}
