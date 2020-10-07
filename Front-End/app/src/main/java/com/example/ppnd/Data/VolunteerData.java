package com.example.ppnd.Data;

public class VolunteerData {
    private String title, date, writer;

    public VolunteerData(String title, String date, String writer){
        this.title = title;
        this.date = date;
        this.writer = writer;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getWriter(){
        return writer;
    }
}
