package com.psl.classes;

public class Partner {
    private String ID_INTEGRATION;
    private String ID_INTEGRATION_VALUE;
    private String STR_INTEGRATION_PARTNER;
    private String STR_DISPLAY_NAME;
    private String STR_URL;

    public String getID_INTEGRATION() {
        return ID_INTEGRATION;
    }

    public void setID_INTEGRATION(String ID_INTEGRATION) {
        this.ID_INTEGRATION = ID_INTEGRATION;
    }

    public String getID_INTEGRATION_VALUE() {
        return ID_INTEGRATION_VALUE;
    }

    public void setID_INTEGRATION_VALUE(String ID_INTEGRATION_VALUE) {
        this.ID_INTEGRATION_VALUE = ID_INTEGRATION_VALUE;
    }

    public String getSTR_INTEGRATION_PARTNER() {
        return STR_INTEGRATION_PARTNER;
    }

    public void setSTR_INTEGRATION_PARTNER(String STR_INTEGRATION_PARTNER) {
        this.STR_INTEGRATION_PARTNER = STR_INTEGRATION_PARTNER;
    }

    public String getSTR_DISPLAY_NAME() {
        return STR_DISPLAY_NAME;
    }

    public void setSTR_DISPLAY_NAME(String STR_DISPLAY_NAME) {
        this.STR_DISPLAY_NAME = STR_DISPLAY_NAME;
    }

    public String getSTR_URL() {
        return STR_URL;
    }

    public void setSTR_URL(String STR_URL) {
        this.STR_URL = STR_URL;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "ID_INTEGRATION='" + ID_INTEGRATION + '\'' +
                ", ID_INTEGRATION_VALUE='" + ID_INTEGRATION_VALUE + '\'' +
                ", STR_INTEGRATION_PARTNER='" + STR_INTEGRATION_PARTNER + '\'' +
                ", STR_DISPLAY_NAME='" + STR_DISPLAY_NAME + '\'' +
                ", STR_URL='" + STR_URL + '\'' +
                '}';
    }
}
