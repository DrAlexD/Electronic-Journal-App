package com.example.electronic_journal.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Event {
    private final Module module;
    private final int type;
    private final int number;
    private final Date startDate;
    private final Date deadlineDate;
    private final int minPoints;
    private final int maxPoints;
    private final int numberOfVariants;
    private long id;

    public Event(Module module, int type, int number, Date startDate, Date deadlineDate,
                 int minPoints, int maxPoints, int numberOfVariants) {
        this.module = module;
        this.type = type;
        this.number = number;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.numberOfVariants = numberOfVariants;
    }

    public Event(long id, Module module, int type, int number, Date startDate, Date deadlineDate,
                 int minPoints, int maxPoints, int numberOfVariants) {
        this.id = id;
        this.module = module;
        this.type = type;
        this.number = number;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.numberOfVariants = numberOfVariants;
    }

    public static int convertTypeToInt(String type) {
        switch (type) {
            case "ДЗ":
                return 0;
            case "ЛР":
                return 1;
            case "РК":
                return 2;
        }

        return -1;
    }

    public static String convertTypeToString(int type) {
        switch (type) {
            case 0:
                return "ДЗ";
            case 1:
                return "ЛР";
            case 2:
                return "РК";
        }

        return "ERROR";
    }

    @NotNull
    @Override
    public String toString() {
        return "Event{" + "type='" + type + '\'' + ", number=" + number + ", startDate=" + startDate +
                ", deadlineDate=" + deadlineDate + ", minPoints=" + minPoints + ", maxPoints=" + maxPoints + '}';
    }

    public long getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public String getTitle() {
        return getTypeString() + number;
    }

    public int getTypeNumber() {
        return type;
    }

    public String getTypeString() {
        return convertTypeToString(type);
    }

    public int getNumber() {
        return number;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getNumberOfVariants() {
        return numberOfVariants;
    }
}
