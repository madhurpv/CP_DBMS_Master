package com.mv.cp_dbms_master;

public class ComplaintsClass {

    static int COMPLAINT_NOT_RESOLVED = 0;
    static int COMPLAINT_RESOLVED = 1;

    String title = "";
    String description = "";
    Long time = -1L;
    int complaint_state = COMPLAINT_NOT_RESOLVED;
    String complaint_maker_number = "-1";
    String complaint_maker_name = "-1";

    public ComplaintsClass(String title, String description, Long time, int complaint_state, String complaint_maker_number, String complaint_maker_name) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.complaint_state = complaint_state;
        this.complaint_maker_number = complaint_maker_number;
        this.complaint_maker_name = complaint_maker_name;
    }
}
