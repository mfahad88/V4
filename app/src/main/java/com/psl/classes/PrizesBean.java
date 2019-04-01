package com.psl.classes;

public class PrizesBean {
    private int points;
    private int amount;
    private String qty;
    private float percent;

    public PrizesBean(int points, int amount, String qty, float percent) {
        this.points = points;
        this.amount = amount;
        this.qty = qty;
        this.percent = percent;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }


    @Override
    public String toString() {
        return "PrizesBean{" +
                "points=" + points +
                ", amount=" + amount +
                ", qty='" + qty + '\'' +
                ", percent=" + percent +
                '}';
    }
}
