package com.example.merosathi;

import java.util.Calendar;

public class DateItem {
    private String dayOfWeek;
    private String dayOfMonth;
    private int year;
    private int month; // 0-indexed
    private int day;   // 1-indexed
    private boolean isSelected;

    public DateItem(String dayOfWeek, String dayOfMonth, int year, int month, int day) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.year = year;
        this.month = month;
        this.day = day;
        this.isSelected = false;
    }

    // Getters and Setters
    public String getDayOfWeek() { return dayOfWeek; }
    public String getDayOfMonth() { return dayOfMonth; }
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }
}
