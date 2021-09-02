package com.example.myquiz.mqUtils;

public class StoreResult {
    private String highscore;
    private String subjectname;
    private String currentdate;
    private String currenttime;
    private String marks;

    public StoreResult() {
    }

    public StoreResult(String highscore, String subjectname, String currentdate, String currenttime, String marks) {
        this.highscore = highscore;
        this.subjectname = subjectname;
        this.currentdate = currentdate;
        this.currenttime = currenttime;
        this.marks = marks;
    }

    public String getHighscore() {
        return highscore;
    }

    public void setHighscore(String highscore) {
        this.highscore = highscore;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
