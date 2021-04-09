package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Semester {
    private final int id;
    private final int year;
    private final boolean isFirstHalf;

    public Semester(int id, int year, boolean isFirstHalf) {
        this.id = id;
        this.year = year;
        this.isFirstHalf = isFirstHalf;
    }

    @NotNull
    @Override
    public String toString() {
        return getFirstHalf() + "/2 " + getYear();
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public boolean isFirstHalf() {
        return isFirstHalf;
    }

    public String getFirstHalf() {
        return isFirstHalf ? "1" : "2";
    }
}
