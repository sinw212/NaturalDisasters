package com.example.ppnd.Data;

import java.util.ArrayList;

public class VolunteerData {
    private String title, date, writer, content;
    private ArrayList<String> img = new ArrayList<>();

    public VolunteerData(String title, String date, String writer,String content, ArrayList<String> img){
        this.title = title;
        this.date = date;
        this.writer = writer;
        this.content = content;
        this.img = img;
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

    public String getContent() {return  content;}

    public ArrayList<String> getImg() { return img; }
}
