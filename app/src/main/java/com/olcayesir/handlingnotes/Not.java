package com.olcayesir.handlingnotes;

public class Not {
    private String Title;
    private String Note;
    private String Date;
    private String Time;

    public Not (String Title,String Note, String Date,String Time){
        this.Date = Date;
        this.Note = Note;
        this.Time = Time;
        this.Title = Title;
    }
    public String getTitle(){
        return Title;
    }

    public String getNote(){
        return Note;
    }
    public  String getDate(){
        return Date;
    }
    public String getTime(){
        return Time;
    }
}
