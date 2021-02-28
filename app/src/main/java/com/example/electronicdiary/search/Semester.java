package com.example.electronicdiary.search;

public class Semester {
    private final int year;
    private final boolean isFirstHalf;

    public Semester(int year, boolean isFirstHalf) {
        this.year = year;
        this.isFirstHalf = isFirstHalf;
    }

    @Override
    public String toString() {
        return (isFirstHalf ? "1" : "2") + " половина " + year;
    }

    public boolean isFirstHalf() {
        return isFirstHalf;
    }

    public String getFirstHalf() {
        return isFirstHalf ? "1" : "2";
    }

    public int getYear() {
        return year;
    }
}
