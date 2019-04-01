package com.psl.classes;

public class PrizesClass {

    private String heading;
    private String position;
    private String value;
    private String cd;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    @Override
    public String toString() {
        return "PrizesClass{" +
                "heading='" + heading + '\'' +
                ", position='" + position + '\'' +
                ", value='" + value + '\'' +
                ", cd='" + cd + '\'' +
                '}';
    }
}
