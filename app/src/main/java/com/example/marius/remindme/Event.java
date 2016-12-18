package com.example.marius.remindme;

/**
 * Created by marius on 18.12.2016.
 */

public class Event {
    private Integer id;
    private String title;
    private String description;
    private String frequency;
    private String frequencyType;
    private String active;
    private String createDate;
    private String nextAlertTime;

    public Event(){}

    public Event(Integer id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNextAlertTime() {
        return nextAlertTime;
    }

    public void setNextAlertTime(String nextAlertTime) {
        this.nextAlertTime = nextAlertTime;
    }

}
