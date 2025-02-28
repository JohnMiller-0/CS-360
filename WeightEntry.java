package com.snhu.cs360_project_miller;

public class WeightEntry {
    private long mId;
    private String mDate;
    private int mWeight;

    public WeightEntry(long id, String date, int weight) {
        mId = id;
        mDate = date;
        mWeight = weight;
    }

    public long getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int newWeight) {
        mWeight = newWeight;
    }
}
