package com.del.ministry.view.filters;

import java.util.Date;

public class AppointmentsFilter {

    private Date from, to;
    private String lastName;
    private String number;
    private boolean onlyActiveNow;

    public AppointmentsFilter() {
    }

    public AppointmentsFilter(Date from, Date to, String lastName, String number, boolean onlyActiveNow) {
        this.from = from;
        this.to = to;
        this.lastName = lastName;
        this.number = number;
        this.onlyActiveNow = onlyActiveNow;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isOnlyActiveNow() {
        return onlyActiveNow;
    }

    public void setOnlyActiveNow(boolean onlyActiveNow) {
        this.onlyActiveNow = onlyActiveNow;
    }
}
