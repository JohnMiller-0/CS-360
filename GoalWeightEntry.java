package com.snhu.cs360_project_miller;

public class GoalWeightEntry {
    private final int goalWeight;
    private final String goalType;

    public GoalWeightEntry(int goalWeight, String goalType) {
        this.goalWeight = goalWeight;
        this.goalType = goalType;
    }

    public int getGoalWeight() {
        return goalWeight;
    }

    public String getGoalType() {
        return goalType;
    }
}