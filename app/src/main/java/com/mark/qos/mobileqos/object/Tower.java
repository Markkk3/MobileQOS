package com.mark.qos.mobileqos.object;

/**
 * Created by tushkevich_m on 11.01.2017.
 */

public class Tower {

    int color;
    long id;

    public Tower(long id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
