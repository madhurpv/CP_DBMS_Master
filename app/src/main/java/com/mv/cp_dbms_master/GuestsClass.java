package com.mv.cp_dbms_master;

public class GuestsClass {

    static boolean PARKING_REQUIRED = true;
    static boolean PARKING_NOT_REQUIRED = false;

    static int NO_VEHICLE = -1;
    static int FOUR_WHEELER = 0;
    static int TWO_WHEELER = 1;


    String nameOfGuest = "";
    long startTime = -1;
    long endTime = -1;
    int numberOfGuests;
    boolean parkingRequired;
    int parkingType;

    public GuestsClass(String nameOfGuest, long startTime, long endTime, int numberOfGuests, boolean parkingRequired, int parkingType){
        this.nameOfGuest = nameOfGuest;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfGuests = numberOfGuests;
        this.parkingRequired = parkingRequired;
        this.parkingType = parkingType;
    }


}
