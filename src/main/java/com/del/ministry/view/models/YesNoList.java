package com.del.ministry.view.models;

import java.util.Objects;

public class YesNoList implements Comparable<YesNoList> {

    public static final YesNoList YES = new YesNoList("Да");
    public static final YesNoList NO = new YesNoList("Нет");

    public static final YesNoList[] LIST = new YesNoList[]{YES, NO};

    private String value;

    private YesNoList(String value) {
        this.value = value;
    }

    public boolean isYes() {
        return Objects.deepEquals(YES.value, value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(YesNoList o) {
        return value.compareTo(o.value);
    }
}
