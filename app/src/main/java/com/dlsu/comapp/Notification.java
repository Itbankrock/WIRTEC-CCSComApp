package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 13/03/2018.
 */

public class Notification {
    private String notifTitle, notifType;

    public Notification(String notifTitle, String notifType) {
        this.notifTitle = notifTitle;
        this.notifType = notifType;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }
}
