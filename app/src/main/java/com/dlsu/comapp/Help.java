package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 11/03/2018.
 */

public class Help {

    private String helpHeader, helpContent;

    public Help(String helpHeader, String helpContent) {
        this.helpHeader = helpHeader;
        this.helpContent = helpContent;
    }

    public String getHelpHeader() {
        return helpHeader;
    }

    public void setHelpHeader(String helpHeader) {
        this.helpHeader = helpHeader;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }
}
