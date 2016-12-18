package com.example.marius.remindme;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by marius on 18.12.2016.
 */

public class EventGenerics {
    //The following constants are used to help identify the index of every value from each array found in the strings.xml resource file.
    public static final int c_ONE_HOUR_RES_INDEX = 0;
    public static final int c_TWO_HOURS_RES_INDEX = 1;
    public static final int c_FOUR_HOURS_RES_INDEX = 2;
    public static final int c_SIX_HOURS_RES_INDEX = 3;
    public static final int c_EIGHT_HOURS_RES_INDEX = 4;
    public static final int c_TWELVE_HOURS_RES_INDEX = 5;
    public static final int c_SIXTEEN_HOURS_RES_INDEX = 6;
    public static final int c_TWENTYTWO_HOURS_RES_INDEX = 7;

    public static final int c_FIVE_MINUTES_RES_INDEX = 0;
    public static final int c_FIFTEEN_MINUTES_RES_INDEX = 1;
    public static final int c_THIRTY_MINUTES_RES_INDEX = 2;
    public static final int c_FOURTYFIVE_MINUTES_RES_INDEX = 3;

    public static final int c_ONE_DAY_RES_INDEX = 0;
    public static final int c_TWO_DAYS_RES_INDEX = 1;
    public static final int c_THREE_DAYS_RES_INDEX = 2;
    public static final int c_FOUR_DAYS_RES_INDEX = 3;
    public static final int c_FIVE_DAYS_RES_INDEX = 4;
    public static final int c_SIX_DAYS_RES_INDEX = 5;
    public static final int c_SEVEN_DAYS_RES_INDEX = 6;
    public static final int c_EIGHT_DAYS_RES_INDEX = 7;
    public static final int c_NINE_DAYS_RES_INDEX = 8;
    public static final int c_TEN_DAYS_RES_INDEX = 9;

    public static final int c_DAYS_FREQUENCY_RES_INDEX = 0;
    public static final int c_HOURS_FREQUENCY_RES_INDEX = 1;
    public static final int c_MINUTES_FREQUENCY_RES_INDEX = 2;

    //Default date format
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //Default active column value
    public static final String activeDefault = "1";

    public static String calculateNextTimeAlert(int frequencyTypeConstant,  String frequency){
        Calendar currentTime = Calendar.getInstance();
        String nextTimeAlert = "";

        switch(frequencyTypeConstant){
            case c_DAYS_FREQUENCY_RES_INDEX:
                currentTime.add(Calendar.DATE, Integer.parseInt(frequency));
                break;
            case c_HOURS_FREQUENCY_RES_INDEX:
                currentTime.add(Calendar.HOUR, Integer.parseInt(frequency));
                break;
            case c_MINUTES_FREQUENCY_RES_INDEX:
                currentTime.add(Calendar.MINUTE, Integer.parseInt(frequency));
        }

        nextTimeAlert = dateFormat.format(currentTime.getTime());
        return nextTimeAlert;
    }
}
