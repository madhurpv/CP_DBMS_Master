package com.mv.cp_dbms_master;

public class NoticesClass {

    String title = "";
    String details = "";
    long time = -1;

    public NoticesClass(String title, long time, String details){
        this.title = title;
        this.time = time;
        this.details = details;
    }

}
