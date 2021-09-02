package com.example.myquiz;

public class ExampleItem {
    private String mScore;
    private String mSubject;
    private String mDate;
    private String mTime;

    public ExampleItem() {
    }

    public ExampleItem(String Score, String Subject, String Date, String Time)
    {
        mScore = Score;
        mSubject = Subject;
        mDate = Date;
        mTime = Time;
    }

    public String getScore()
    {
        return mScore;
    }
    public String getSubject()
    {
        return mSubject;
    }
    public String getDate()
    {
        return mDate;
    }
    public String getTime()
    {
        return mTime;
    }
}
