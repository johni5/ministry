package com.del.ministry.view.models;

/**
 * Created by DodolinEL
 * date: 03.10.2018
 */
public class NumberItem {

    private Number number;
    private String title;
    private boolean common;

    public NumberItem(Number number) {
        this(number, number.toString());
    }

    public NumberItem(Number number, String title, boolean common) {
        this.number = number;
        this.title = title;
        this.common = common;
    }

    public NumberItem(Number number, String title) {
        this(number, title, false);
    }

    public Number getNumber() {
        return number;
    }

    public Number getNumber(Number ifCommonValue) {
        return !common ? number : ifCommonValue;
    }

    public boolean isCommon() {
        return common;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
