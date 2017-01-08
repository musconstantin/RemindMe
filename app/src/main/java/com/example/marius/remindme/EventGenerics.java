package com.example.marius.remindme;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by marius on 18.12.2016.
 */

public class EventGenerics {
    //The following constants are used to help identify the index of every value from each array found in the strings.xml resource file.

    public static final int c_DAYS_FREQUENCY_RES_INDEX = 0;
    public static final int c_HOURS_FREQUENCY_RES_INDEX = 1;
    public static final int c_MINUTES_FREQUENCY_RES_INDEX = 2;

    //Default date format
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //Default active column value

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

    public static int getFrequencyIndex(String frequencyType){
        switch(frequencyType){
            case "days":
                return 0;
            case "hours":
                return 1;
            case "minutes":
                return 2;
            default:
                return -1;
        }
    }

    public static long getNextAlertTime(int frequencyTypeConstant, String frequency){
        try {
            return dateFormat.parse(calculateNextTimeAlert(frequencyTypeConstant, frequency)).getTime();
        }catch (Exception ex){
            Log.e("Next alert exception", ex.toString());
            return 0;
        }
    }

    public static long getFrequencyInMillis(int frequencyTypeConstant, String frequency){
        switch (frequencyTypeConstant){
            case c_DAYS_FREQUENCY_RES_INDEX:
                return TimeUnit.DAYS.toMillis(Integer.parseInt(frequency));
            case c_HOURS_FREQUENCY_RES_INDEX:
                return TimeUnit.HOURS.toMillis(Integer.parseInt(frequency));
            case c_MINUTES_FREQUENCY_RES_INDEX:
                return TimeUnit.MINUTES.toMillis(Integer.parseInt(frequency));
            default:
                return -1;
        }
    }
}
