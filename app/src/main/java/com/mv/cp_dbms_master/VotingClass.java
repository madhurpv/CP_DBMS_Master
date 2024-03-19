package com.mv.cp_dbms_master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VotingClass {

    static boolean VOTED = true;
    static boolean NOT_VOTED = false;


    String title = "";
    String details = "";
    long startTime = -1;
    long endTime = -1;
    List<String> options = new ArrayList<>();
    int selection;
    boolean voted;
    HashMap<String, Long> votedOptions = new HashMap<>();

    public VotingClass(String title, long startTime, long endTime, String details, List<String> options, int selection, boolean voted, HashMap<String, Long> votedOptions){
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
        this.options = options;
        this.selection = selection;
        this.voted = voted;
        this.votedOptions = votedOptions;
    }


}
